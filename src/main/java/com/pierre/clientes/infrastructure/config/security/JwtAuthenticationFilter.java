package com.pierre.clientes.infrastructure.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        return  jwtUtil.validateToken(token)
                .flatMap(valid -> {
                   if (!valid){
                       return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido"));
                   }
                   return jwtUtil.extractUsername(token)
                           .map(username-> new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()))
                           .flatMap(auth -> chain.filter(exchange)
                                   .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
                });

    }
}
