package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import reactor.core.publisher.Mono;

public interface GetCustomerByIdUseCase {
    Mono<CustomerResponseDTO> execute(String id);
}
