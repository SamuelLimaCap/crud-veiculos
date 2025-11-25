package com.support.compracarros.dto.req;

import java.math.BigDecimal;

public record CreateAnuncioVeiculoReq(
        String placa,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal price
) {
}
