package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.req.CreateUserRequest;
import com.support.compracarros.dto.req.LoginUserRequest;
import com.support.compracarros.dto.res.LoginResponse;
import com.support.compracarros.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

   @PostMapping("signin")
   public ResponseEntity<Result<LoginResponse>> login(@RequestBody @Valid LoginUserRequest loginUserRequest) {
       var content = userService.signInUser(loginUserRequest);
       return ResponseEntity.ok(Result.with("logado com sucesso", content));
   }

   @PostMapping("logout")
   public ResponseEntity<Result<Void>> logout() {
       // @Todo remover token de acesso enviado no header do banco de dados;
       return ResponseEntity.ok(Result.with("deslogado com sucesso"));
   }

    @PostMapping("signup")
    public ResponseEntity<Result<Void>> signUp(@RequestBody @Valid CreateUserRequest createUserRequest) {
       userService.signUpUser(createUserRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(Result.with("Usuário criado com sucesso"));
    }
}
