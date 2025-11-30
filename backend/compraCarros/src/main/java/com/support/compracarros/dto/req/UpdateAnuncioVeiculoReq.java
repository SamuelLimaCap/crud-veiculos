package com.support.compracarros.dto.req;

import com.support.compracarros.models.AnuncioVeiculoState;
import com.support.compracarros.models.UpdateAnuncioType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAnuncioVeiculoReq {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;
    private BigDecimal price;
    private MultipartFile image;
    private AnuncioVeiculoState estado;
    private UpdateAnuncioType type;

    public static UpdateAnuncioVeiculoReq ofPut(
            Long id,
            String placa,
            String marca,
            String modelo,
            Integer ano,
            String cor,
            BigDecimal price,
            MultipartFile image,
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
                image,
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
            MultipartFile image,
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
                image,
                estado,
                UpdateAnuncioType.PATCH
        );
    }
}
