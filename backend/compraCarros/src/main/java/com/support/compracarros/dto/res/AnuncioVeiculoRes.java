package com.support.compracarros.dto.res;

import com.support.compracarros.entities.AnuncioVeiculo;
import com.support.compracarros.models.AnuncioVeiculoState;

import java.math.BigDecimal;
import java.util.Base64;

public record AnuncioVeiculoRes(
        Long id,
        Long userId,
        String placa,
        BigDecimal price,
        String moeda,
        BigDecimal kmRodados,
        String imageBase64,
        String imageName,
        String imageType,
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
                anuncioVeiculo.getKmRodados(),
                Base64.getEncoder().encodeToString(anuncioVeiculo.getImageData()),
                anuncioVeiculo.getImageName(),
                anuncioVeiculo.getImageType(),
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
