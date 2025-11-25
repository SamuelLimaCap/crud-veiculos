package com.support.compracarros.services.impl;

import com.support.compracarros.config.UserDetailsImpl;
import com.support.compracarros.dto.req.CreateUserRequest;
import com.support.compracarros.dto.req.LoginUserRequest;
import com.support.compracarros.dto.res.LoginResponse;
import com.support.compracarros.dto.res.UserResponse;
import com.support.compracarros.entities.AccessToken;
import com.support.compracarros.entities.RefreshToken;
import com.support.compracarros.entities.User;
import com.support.compracarros.exceptions.APIAuthException;
import com.support.compracarros.exceptions.BadCredentialsException;
import com.support.compracarros.models.FieldErrors;
import com.support.compracarros.repositories.AccessTokenRepository;
import com.support.compracarros.repositories.RefreshTokenRepository;
import com.support.compracarros.repositories.UserRespository;
import com.support.compracarros.services.UserService;
import com.support.compracarros.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRespository userRespository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.refreshTokenExpSeconds}")
    private long refreshTokenExpirationSeconds;

    @Value("${app.jwtExpirationSeconds}")
    private long jwtExpirationSeconds;


    @Override
    public void signUpUser(CreateUserRequest createUserRequest) {
        if ((createUserRequest.password().length() < 8)) {
            throw new BadCredentialsException("senha menor que 8",
                    FieldErrors.builder()
                            .add("password", "senha precisa ter mais que 8 caracteres")
                            .build()
            );
        }

        userRespository.findByEmail(createUserRequest.email()).ifPresent((user) -> {
            throw new BadCredentialsException("email já existe",
                    FieldErrors.builder()
                            .add("email", "email já existe")
                            .build()
            );
        });

        var newUser = User.builder()
                .username(createUserRequest.fullname())
                .email(createUserRequest.email())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .enabled(true)
                .build();

        userRespository.save(newUser);
    }

    @Override
    public LoginResponse signInUser(LoginUserRequest loginUserRequest) {
        var usernamePasswordAuthToken = new UsernamePasswordAuthenticationToken(loginUserRequest.email(), loginUserRequest.password());
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthToken);
        } catch (AuthenticationException e) {
            if (e.getCause() instanceof org.springframework.security.authentication.BadCredentialsException) {
                throw new APIAuthException("Email e/ou senha incorreta(s)");
            } else {
                throw new APIAuthException(e.getMessage());
            }
        }

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var user = userDetails.getUser();

        var accessTokenString = jwtTokenProvider.generateToken(userDetails);
        var accessTokenEntity = AccessToken.builder()
                .token(accessTokenString)
                .expiresAt(Instant.now().plusSeconds(jwtExpirationSeconds))
                .user(user)
                .build();
        var refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .expiresAt(Instant.now().plusSeconds(refreshTokenExpirationSeconds))
                .build();

        accessTokenRepository.save(accessTokenEntity);
        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponse(
                accessTokenString, refreshTokenEntity.getId().toString(),
                new UserResponse(user.getUsername(), user.getEmail())
        );

    }

}
