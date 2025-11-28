package com.support.compracarros.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.compracarros.config.SecurityConfig;
import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


@Component
public class JwtAuthtenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    public JwtAuthtenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(SecurityConfig.PUBLIC_ENDPOINTS).anyMatch(
                stringURI -> PathPatternRequestMatcher.withDefaults().matcher(stringURI).matches(request)
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = getJwtFromRequest(request);

            if (token == null) {
                throw new Exception("Token não encontrado no header Authorization");
            }

            var email = jwtTokenProvider.getEmailFromToken(token);
            var permissions = jwtTokenProvider.getAuthoritiesFromToken(token);

            var authorities = permissions.stream()
                    .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.name()))
                    .collect(Collectors.toList());

            var authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.err("Token de acesso ausente, invalido ou expirado")));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        var bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
