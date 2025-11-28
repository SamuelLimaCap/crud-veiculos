package com.support.compracarros.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FipeResponseAno(
        @JsonProperty("codigo")
        String codigo,
        @JsonProperty("nome")
        String nome
) {

}
