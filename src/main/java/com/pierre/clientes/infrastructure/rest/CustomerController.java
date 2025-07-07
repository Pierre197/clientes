package com.pierre.clientes.infrastructure.rest;

import com.pierre.clientes.application.dto.CustomerRequestDTO;
import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.usecase.implementation.CreateCustomerUseCaseImpl;
import com.pierre.clientes.application.usecase.GetAllCustomerUseCase;
import com.pierre.clientes.application.usecase.GetCustomerByIdUseCase;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.infrastructure.config.web.HeaderExtractor;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "Operaciones relacionadas a Clientes")
public class CustomerController {

    private final CreateCustomerUseCaseImpl createCustomerUseCase;
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final GetAllCustomerUseCase getAllCustomerUseCase;
    private final HeaderExtractor headerExtractor;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerResponseDTO> create(
            @Valid @RequestBody CustomerRequestDTO dto,
            @Parameter(hidden = true) @RequestHeader HttpHeaders headers){
        RequestHeaders requestHeaders = headerExtractor.extract(headers);
        return createCustomerUseCase.execute(dto, requestHeaders);
    }

    @GetMapping("/{id}")
    public Mono<CustomerResponseDTO> getById(@PathVariable String id){
        return getCustomerByIdUseCase.execute(id);
    }

    @GetMapping()
    public Flux<CustomerResponseDTO> getAll(@Parameter(hidden = true) @RequestHeader HttpHeaders headers){
        RequestHeaders requestHeaders = headerExtractor.extract(headers);
        return getAllCustomerUseCase.getAll(requestHeaders);
    }
}
