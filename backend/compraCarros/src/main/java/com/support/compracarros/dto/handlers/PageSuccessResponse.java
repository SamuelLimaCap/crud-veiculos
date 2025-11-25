package com.support.compracarros.dto.handlers;

import java.util.List;

public record PageSuccessResponse<T>(
        String status,
        String message,
        List<T> content,
        PageDetails pageInfo
) {
    public record PageDetails(
            int pageNum,
            int size,
            long totalElements,
            int totalPages
    ) {}
}
