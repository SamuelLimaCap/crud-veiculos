package com.support.compracarros.controllers;

import com.support.compracarros.dto.handlers.ErrorResponse;
import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.dto.handlers.SuccessResponse;
import com.support.compracarros.dto.req.CreateUserRequest;
import com.support.compracarros.dto.req.LoginUserRequest;
import com.support.compracarros.dto.res.LoginResponse;
import com.support.compracarros.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Authentication",
        description = "Endpoints para autenticação de usuários (login, logout e registro)"
)
public class AuthController {

    private final UserService userService;

   @PostMapping("signin")
   @Operation(
           summary = "Fazer login",
           description = "Autentica um usuário com email e senha. Retorna um token JWT para requisições autenticadas.",
           tags = {"Authentication"}
   )
   @ApiResponses(value = {
           @ApiResponse(
                   responseCode = "200",
                   description = "Login realizado com sucesso",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(implementation = SuccessResponse.class)
                   )
           ),
           @ApiResponse(
                   responseCode = "401",
                   description = "Credenciais inválidas (email ou senha incorretos)",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(
                                   implementation = ErrorResponse.class
                           )
                   )
           ),
           @ApiResponse(
                   responseCode = "400",
                   description = "Dados de entrada inválidos",
                   content = @Content(
                           mediaType = "application/json",
                           schema = @Schema(
                                   implementation = ErrorResponse.class
                           )
                   )
           )
   })
   public ResponseEntity<Result<LoginResponse>> login(@RequestBody @Valid LoginUserRequest loginUserRequest) {
       var content = userService.signInUser(loginUserRequest);
       return ResponseEntity.ok(Result.with("logado com sucesso", content));
   }

    @Operation(
            summary = "Fazer logout",
            description = "Desconecta o usuário autenticado. O token JWT se torna inválido.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                   implementation = SuccessResponse.class
                            )
                    )
            ),
    })
   @PostMapping("logout")
   public ResponseEntity<Result<Void>> logout() {
       return ResponseEntity.ok(Result.with("deslogado com sucesso"));
   }


    @PostMapping("signup")
    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário. Email deve ser único. Senha será criptografada.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = SuccessResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos ou email já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            ),
    })
    public ResponseEntity<Result<Void>> signUp(@RequestBody @Valid CreateUserRequest createUserRequest) {
       userService.signUpUser(createUserRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(Result.with("Usuário criado com sucesso"));
    }
}
