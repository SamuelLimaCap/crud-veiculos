package com.support.compracarros.dto.req;

import jakarta.validation.constraints.Email;

public record CreateUserRequest(String fullname,
                                @Email(message = "Email invalido")
                                String email, String password) {
}
