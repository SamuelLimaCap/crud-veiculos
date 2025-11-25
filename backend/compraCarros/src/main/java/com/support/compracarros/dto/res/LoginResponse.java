package com.support.compracarros.dto.res;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        UserResponse userDetails
) {
}
