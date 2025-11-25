package com.support.compracarros.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginUserRequest(
        @Email(message = "email inválido")
        @NotNull(message = "Não pode estar vazio")
        String email,
        @NotNull(message = "Não pode estar vazio")
        @Length(min = 8, message = "Mínimo 8 caracteres")
        String password) {
}
