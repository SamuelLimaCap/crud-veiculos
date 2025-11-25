package com.support.compracarros.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PedidoAnuncioReq(
        Long idAnuncio,
        String nome,
        String email,
        @NotNull
        @NotBlank
        String telefone,
        @NotBlank
        String mensagem
) {
}
