package com.support.compracarros.services;

import com.support.compracarros.dto.handlers.PageSuccessResponse;
import com.support.compracarros.dto.req.PedidoAnuncioReq;
import com.support.compracarros.dto.res.PedidoAnuncioRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PedidoCompraService {

    public PedidoAnuncioRes pedirCompra(PedidoAnuncioReq pedidoAnuncio);

    public PageSuccessResponse<List<PedidoAnuncioRes>> getAll(int size, int page);
}
