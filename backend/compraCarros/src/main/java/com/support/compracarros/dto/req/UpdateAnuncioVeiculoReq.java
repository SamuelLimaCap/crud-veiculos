package com.support.compracarros.dto.req;

import com.support.compracarros.models.AnuncioVeiculoState;
import com.support.compracarros.models.UpdateAnuncioType;
import lombok.With;

import java.math.BigDecimal;

@With
public record UpdateAnuncioVeiculoReq(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal price,
        AnuncioVeiculoState estado,
        UpdateAnuncioType type
) {

    public static UpdateAnuncioVeiculoReq ofPut(
            Long id,
            String placa,
            String marca,
            String modelo,
            Integer ano,
            String cor,
            BigDecimal price,
            AnuncioVeiculoState estado
    ) {

        return new UpdateAnuncioVeiculoReq(
                id,
                placa,
                marca,
                modelo,
                ano,
                cor,
                price,
                estado,
                UpdateAnuncioType.PUT
        );
    }
    public static UpdateAnuncioVeiculoReq ofPatch(
            Long id,
            String placa,
            String marca,
            String modelo,
            Integer ano,
            String cor,
            BigDecimal price,
            AnuncioVeiculoState estado
    ) {
        return new UpdateAnuncioVeiculoReq(
                id,
                placa,
                marca,
                modelo,
                ano,
                cor,
                price,
                estado,
                UpdateAnuncioType.PATCH
        );
    }
}
