package com.support.compracarros.dto.handlers;

public record ErrorResponse(
        String status,
        String type,
        String message
) implements Result<Void> {
}
