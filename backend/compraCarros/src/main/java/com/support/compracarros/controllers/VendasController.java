package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.req.CreateAnuncioVeiculoReq;
import com.support.compracarros.dto.req.UpdateAnuncioVeiculoReq;
import com.support.compracarros.dto.res.AnuncioVeiculoRes;
import com.support.compracarros.models.UpdateAnuncioType;
import com.support.compracarros.services.AnuncioVeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/vendas")
@RequiredArgsConstructor
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
}
