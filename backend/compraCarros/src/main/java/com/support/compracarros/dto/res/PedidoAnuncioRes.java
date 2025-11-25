package com.support.compracarros.dto.res;

import com.support.compracarros.entities.PedidoCompra;

public record PedidoAnuncioRes(
        Long id,
        Long userIdDoPedido,
        String fullName,
        String email,
        String telefone,
        String mensagem,
        Long anuncioVeiculoId
) {
    public static PedidoAnuncioRes of(PedidoCompra pd) {
        return new PedidoAnuncioRes(
                pd.getId(),
                pd.getUser().getId(),
                pd.getFullname(),
                pd.getUserEmail(),
                pd.getTelephone(),
                pd.getMessage(),
                pd.getAnuncioVeiculo().getId()
        );
    }
}
