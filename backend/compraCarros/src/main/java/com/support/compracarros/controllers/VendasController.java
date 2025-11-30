package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.req.CreateAnuncioVeiculoReq;
import com.support.compracarros.dto.req.UpdateAnuncioVeiculoReq;
import com.support.compracarros.dto.res.AnuncioVeiculoRes;
import com.support.compracarros.models.AnuncioVeiculoState;
import com.support.compracarros.models.UpdateAnuncioType;
import com.support.compracarros.services.AnuncioVeiculoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/vendas")
@RequiredArgsConstructor
@Tag(
        name = "Anuncios",
        description = "Endpoints para manipular e consultar os anuncios dos veiculos"
)
public class VendasController {

    private final AnuncioVeiculoService anuncioVeiculoService;

    @PostMapping("anunciar")
    public ResponseEntity<Result<AnuncioVeiculoRes>> anunciarVeiculo(@ModelAttribute CreateAnuncioVeiculoReq anuncioVeiculo) throws IOException {

        var res = anuncioVeiculoService.anunciarVeiculo(anuncioVeiculo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Result.with("anuncio criado com sucesso!", res));
    }

    @PutMapping("alterar-infomações")
    public ResponseEntity<Result<AnuncioVeiculoRes>> alterarInformacoes(@ModelAttribute UpdateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException {
        var anuncioViaPut = anuncioVeiculoReq.withType(UpdateAnuncioType.PUT);
        var res = anuncioVeiculoService.updateVeiculo(anuncioViaPut);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.with("Anuncio atualizado com sucesso!", res));
    }

    @PatchMapping
    public ResponseEntity<Result<AnuncioVeiculoRes>> alterarInformacoesPatch(@ModelAttribute UpdateAnuncioVeiculoReq anuncioVeiculoReq) throws IOException {
        var anuncioViaPatch = anuncioVeiculoReq.withType(UpdateAnuncioType.PATCH);
        var res = anuncioVeiculoService.updateVeiculo(anuncioViaPatch);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Result.with("Anuncio atualizado com sucesso!", res));
    }

    @PatchMapping("{id}/cliente/{clienteId}")
    public ResponseEntity<Result<Void>> FinalizarAnuncioComCliente(@PathVariable Integer id, @PathVariable Integer clienteId) throws IOException {
        anuncioVeiculoService.mudarEstadoAnuncio(AnuncioVeiculoState.ENCERRADO, Long.valueOf(id), Optional.of(Long.valueOf(clienteId)))   ;

        return ResponseEntity.ok(Result.with("Anuncio finalizado com sucesso!", null));
    }


    @DeleteMapping("invalidar-anuncio/{id}")
    public ResponseEntity<Result<Void>> invalidarAnuncio(@PathVariable("id") Long anuncioId) {
        var isDeleted = anuncioVeiculoService.deletarAnuncio(anuncioId);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Result.with("deletado com sucesso!"));
        }

        throw new RuntimeException("Erro ao deletar anuncio");
    }

    @GetMapping
    public ResponseEntity<Result<List<AnuncioVeiculoRes>>> getAllWithPagination(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size
                                     ) {
        var res = anuncioVeiculoService.getAll(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<AnuncioVeiculoRes>> getById(@PathVariable("id") Long anuncioId) {
        var res = anuncioVeiculoService.findById(anuncioId);
        return ResponseEntity.ok(Result.with("anuncio retornado com sucesso!", res));
    }

    @GetMapping("/getByCreatedUser/{idUser}")
    public ResponseEntity<Result<List<AnuncioVeiculoRes>>> getByUserCreatedId(@PathVariable("idUser") Long idUser) {
        var res = anuncioVeiculoService.findByCreatedUserId(idUser);

        return ResponseEntity.ok(Result.with("anuncios retornado com sucesso!", res));

    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "ordenarPor", required = false) String ordenarPor,
            @RequestParam(value = "precoMin", required = false) BigDecimal precoMin,
            @RequestParam(value = "precoMax", required = false) BigDecimal precoMax,
            @RequestParam(value = "kmMin", required = false) BigDecimal kmMin,
            @RequestParam(value = "kmMax", required = false) BigDecimal kmMax,
            @RequestParam(value = "anoMin", required = false) Integer anoMin,
            @RequestParam(value = "anoMax", required = false) Integer anoMax,
            @RequestParam(value = "marca", required = false) String marca,
            @RequestParam(value = "modelo", required = false) String modelo) {

        return ResponseEntity.ok(anuncioVeiculoService.filtrar(page, size, ordenarPor, precoMin, precoMax,
                kmMin, kmMax, anoMin, anoMax, marca, modelo));
    }
}
