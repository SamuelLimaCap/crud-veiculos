package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.res.ModeloFipeRes;
import com.support.compracarros.models.FipeResponseAno;
import com.support.compracarros.services.FipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/fipe")
@RequiredArgsConstructor
public class FipeController {

    private final FipeService fipeService;

    @GetMapping("marca/{id}/modelos")
    public ResponseEntity<Result<List<ModeloFipeRes>>> getModelo(@PathVariable String id) {
        var res = fipeService.getModelosByMarcaId(id);

        return ResponseEntity.ok(Result.with("sucesso!", res.stream().map(ModeloFipeRes::of).toList()));
    }

    @GetMapping("marca/{marcaId}/modelo/{modeloId}/anos")
    public ResponseEntity<Result<List<FipeResponseAno>>> getAnos(@PathVariable String marcaId, @PathVariable String modeloId) {
        var res = fipeService.getAnosByModeloId(modeloId, marcaId);
        return ResponseEntity.ok(Result.with("sucesso!", res));
    }

}
