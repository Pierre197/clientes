package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import reactor.core.publisher.Flux;

public interface GetAllCustomerUseCase {
    Flux<CustomerResponseDTO> getAll(RequestHeaders headers);
}
