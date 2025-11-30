package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.handlers.SuccessResponse;
import com.support.compracarros.dto.req.PedidoAnuncioReq;
import com.support.compracarros.dto.res.PedidoAnuncioRes;
import com.support.compracarros.services.PedidoCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/compras")
@RequiredArgsConstructor
public class PedidoComprasController {

    private final PedidoCompraService pedidoCompraService;

    @GetMapping
    public ResponseEntity<Result<List<PedidoAnuncioRes>>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoCompraService.getAll());
    }

    @GetMapping("/anuncio/{id}")
    public ResponseEntity<Result<List<PedidoAnuncioRes>>> getAllByAnuncio(@PathVariable Long id) {
        var res = pedidoCompraService.getAllByAnuncio(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.with("pedido de compras retornado com sucesso!", res));
    }

    @GetMapping("/{id}")
    public void getById(@PathVariable Long id) {
        // TODO: fazer by ID
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> desistirDaCompra(@PathVariable Long id) {
        pedidoCompraService.desistirDaCompra(id);

        return ResponseEntity.ok(Result.with("compra desistida"));
    }


    @PostMapping
    public ResponseEntity<Result<PedidoAnuncioRes>> fazerPedido(@RequestBody PedidoAnuncioReq pedidoAnuncioReq) {
        var res = pedidoCompraService.pedirCompra(pedidoAnuncioReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Result.with("Pedido feito com sucesso!", res));
    }

}