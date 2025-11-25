package com.support.compracarros.dto.res;

public record VeiculoResponse(
        Long id,
        String tipo,
        String marca,
        String modelo,
        Integer ano,
        String cor
) {
}
