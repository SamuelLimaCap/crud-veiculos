package com.support.compracarros.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnuncioVeiculoReq{
        private String placa;
        private String marca;
        private String modelo;
        private String combustivel;
        private Integer ano;
        private String cor;
        private BigDecimal kmRodados;
        private BigDecimal price;
        private MultipartFile image;
}
