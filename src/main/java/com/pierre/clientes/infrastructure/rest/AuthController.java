package com.pierre.clientes.infrastructure.rest;

import com.pierre.clientes.infrastructure.config.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas al login y generación de token JWT")
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    @Operation(summary = "Login simulado", description = "Retorna un JWT válido para pruebas")
    public Mono<ResponseEntity<String>> login(@RequestParam String username){
        return jwtUtil.generateToken(username)
                .map(token -> ResponseEntity.ok("Bearer " + token));
    }
}
