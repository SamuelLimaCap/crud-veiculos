package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.Result;
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
    public ResponseEntity<Result<List<PedidoAnuncioRes>>>getAll(@RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "0") int page
                       ) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(pedidoCompraService.getAll(size, page));
    }

    @GetMapping("/anuncio/{id}")
    public void getAllByAnuncio(@PathVariable Long id) {
        // TODO: fazer List By Anuncio
    }

    @GetMapping("/{id}")
    public void getById(@PathVariable Long id){
        // TODO: fazer by ID
    }

    @PatchMapping
    public void desistirDaCompra() {
        // TODO: fazer desistir da compra
    }

    @PostMapping
    public ResponseEntity<Result<PedidoAnuncioRes>> fazerPedido(@RequestBody PedidoAnuncioReq pedidoAnuncioReq) {
        var res =pedidoCompraService.pedirCompra(pedidoAnuncioReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Result.with("Pedido feito com sucesso!", res));
    }

}