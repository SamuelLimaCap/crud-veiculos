package com.support.compracarros.services;

import com.support.compracarros.dto.handlers.PageSuccessResponse;
import com.support.compracarros.dto.handlers.SuccessResponse;
import com.support.compracarros.dto.req.PedidoAnuncioReq;
import com.support.compracarros.dto.res.PedidoAnuncioRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PedidoCompraService {

    public PedidoAnuncioRes pedirCompra(PedidoAnuncioReq pedidoAnuncio);

    public SuccessResponse<List<PedidoAnuncioRes>> getAll();

    public List<PedidoAnuncioRes> getAllByAnuncio(Long id);

    public void desistirDaCompra(Long id);
}
