package com.support.compracarros.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.support.compracarros.exceptions.APIAuthException;
import com.support.compracarros.models.UserPermissionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.issuer}")
    private String issuer;

    @Value("${app.jwtExpirationSeconds}")
    private long jwtExpirationSeconds;

    public String generateToken(UserDetails userDetails)  {
        var algorithm = Algorithm.HMAC256(jwtSecret);
        var permissions = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .map(UserPermissionEnum::valueOf)
                .collect(Collectors.toSet());

        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(jwtExpirationSeconds))
                .withSubject(userDetails.getUsername())
                .withClaim("authorities", grantedAuthoritiesToListPermissions(userDetails.getAuthorities()))
                .sign(algorithm);
    }

    public String getEmailFromToken(String token) {
        var algorithm = Algorithm.HMAC256(jwtSecret);
        try {
            return JWT.require(algorithm).withIssuer(issuer).build().verify(token).getSubject();
        } catch (JWTVerificationException e) {
            throw new APIAuthException("Token invalido/expirado");
        }
    }

    public List<UserPermissionEnum> getAuthoritiesFromToken(String token) {

        List<UserPermissionEnum> permissions;

        try {
            var algorithm =  Algorithm.HMAC256(jwtSecret);
            var decodedJWT = JWT.require(algorithm).build().verify(token);

            return Arrays.asList(decodedJWT.getClaim("authorities").asArray(UserPermissionEnum.class));
        } catch (Exception e) {
            throw new APIAuthException("Permissão invalida");
        }

    }

    public boolean validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(jwtSecret);

            var decodedJWT = JWT.require(algorithm).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> grantedAuthoritiesToListPermissions(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(aut -> {
            return aut.getAuthority().startsWith("ROLE_") ? aut.getAuthority().substring(5) : aut.getAuthority();
        }).collect(Collectors.toList());
    }
}
