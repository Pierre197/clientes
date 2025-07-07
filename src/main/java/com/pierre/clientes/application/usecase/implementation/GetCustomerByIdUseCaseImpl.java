package com.pierre.clientes.application.usecase.implementation;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.GetCustomerByIdUseCase;
import com.pierre.clientes.domain.model.shared.TransactionCode;
import com.pierre.clientes.domain.model.shared.UseCaseTransactionCode;
import com.pierre.clientes.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@UseCaseTransactionCode(TransactionCode.GET)
public class GetCustomerByIdUseCaseImpl implements GetCustomerByIdUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerResponseDTO> execute(String id){
        return customerRepository.findById(id)
                .map(customerMapper::toResponse);
    }
}
