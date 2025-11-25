package com.support.compracarros.dto.handlers;


public record SuccessResponse<T>(
        String status,
        String message,
        T content
) implements Result<T> {
}
