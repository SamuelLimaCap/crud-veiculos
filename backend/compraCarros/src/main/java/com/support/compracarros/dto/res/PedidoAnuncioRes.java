package com.support.compracarros.dto.res;

import com.support.compracarros.entities.PedidoCompra;
import com.support.compracarros.models.AnuncioVeiculoState;

public record PedidoAnuncioRes(
        Long id,
        Long userIdDoPedido,
        String fullName,
        String email,
        String telefone,
        String mensagem,
        Long anuncioVeiculoId,
        AnuncioVeiculoState estadoDoAnuncio,
        boolean disabled
) {
    public static PedidoAnuncioRes of(PedidoCompra pd) {
        return new PedidoAnuncioRes(
                pd.getId(),
                pd.getUser().getId(),
                pd.getFullname(),
                pd.getUserEmail(),
                pd.getTelephone(),
                pd.getMessage(),
                pd.getAnuncioVeiculo().getId(),
                pd.getAnuncioVeiculo().getEstado(),
                pd.isDisabled()
        );
    }
}
