package com.support.compracarros.dto.handlers;

import java.util.List;
import java.util.Map;

public record ValidationErrorResponse(
        String status,
        String type,
        Map<String, List<String>> errorFields
) implements Result<Void> {
}
