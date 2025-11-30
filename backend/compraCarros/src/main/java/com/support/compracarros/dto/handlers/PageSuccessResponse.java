package com.support.compracarros.dto.handlers;

import java.util.List;

public record PageSuccessResponse<T>(
        String status,
        String message,
        T content,
        PageDetails pageInfo
) implements Result<T> {
    public record PageDetails(
            int pageNum,
            int size,
            long totalElements,
            int totalPages
    ) {}
}
