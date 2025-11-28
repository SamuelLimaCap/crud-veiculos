package com.support.compracarros.dto.res;

import com.support.compracarros.entities.Modelos;

public record ModeloFipeRes(String modelo, String codigo) {

    public static ModeloFipeRes of(Modelos modelos) {
        return new ModeloFipeRes(modelos.getLabel(), String.valueOf(modelos.getValue()));
    }
}
