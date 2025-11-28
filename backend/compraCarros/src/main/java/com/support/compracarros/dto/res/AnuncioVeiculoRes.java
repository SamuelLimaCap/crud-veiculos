package com.support.compracarros.dto.res;

import com.support.compracarros.entities.AnuncioVeiculo;
import com.support.compracarros.models.AnuncioVeiculoState;

import java.math.BigDecimal;

public record AnuncioVeiculoRes(
        Long id,
        Long userId,
        String placa,
        BigDecimal price,
        String moeda,
        AnuncioVeiculoState estado,
        VeiculoResponse veiculo
) {
    public static AnuncioVeiculoRes of(AnuncioVeiculo anuncioVeiculo) {
        return new AnuncioVeiculoRes(
                anuncioVeiculo.getId(),
                anuncioVeiculo.getUser().getId(),
                anuncioVeiculo.getPlaca(),
                anuncioVeiculo.getPrice(),
                anuncioVeiculo.getMoeda(),
                anuncioVeiculo.getEstado(),
                new VeiculoResponse(
                        anuncioVeiculo.getVeiculo().getId(),
                        anuncioVeiculo.getVeiculo().getTipo(),
                        anuncioVeiculo.getVeiculo().getMarca(),
                        anuncioVeiculo.getVeiculo().getModelo(),
                        anuncioVeiculo.getVeiculo().getAno(),
                        anuncioVeiculo.getVeiculo().getCor(),
                        anuncioVeiculo.getVeiculo().getCombustivel()
                )
        );
    }
}
