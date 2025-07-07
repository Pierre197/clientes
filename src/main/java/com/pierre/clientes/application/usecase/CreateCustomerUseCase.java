package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerRequestDTO;
import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import reactor.core.publisher.Mono;

public interface CreateCustomerUseCase {
    Mono<CustomerResponseDTO> execute(CustomerRequestDTO dto, RequestHeaders headers);
}
