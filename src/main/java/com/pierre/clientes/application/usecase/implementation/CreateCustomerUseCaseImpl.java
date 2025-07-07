package com.pierre.clientes.application.usecase.implementation;

import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.CreateCustomerUseCase;
import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.domain.model.shared.TransactionCode;
import com.pierre.clientes.domain.model.shared.UseCaseTransactionCode;
import com.pierre.clientes.domain.repository.CustomerRepository;
import com.pierre.clientes.application.dto.CustomerRequestDTO;
import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.domain.util.TransactionCodeResolver;
import com.pierre.clientes.infrastructure.messaging.kafka.publisher.ReactiveKafkaEventPublisher;
import com.pierre.clientes.infrastructure.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
@UseCaseTransactionCode(TransactionCode.CREATE)
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ReactiveKafkaEventPublisher eventPublisher;

    @Override
    public Mono<CustomerResponseDTO> execute(CustomerRequestDTO dto, RequestHeaders headers){
        Customer customer = customerMapper.toDomain(dto);
        return customerRepository.save(customer)
                .flatMap(saved -> {
                   CustomerResponseDTO response = customerMapper.toResponse(saved);
                   return Mono.zip(
                           Mono.fromSupplier(()-> JsonUtils.safeWriteASString(dto)),
                           Mono.fromSupplier(()-> JsonUtils.safeWriteASString(response))
                   ).flatMap(tuple -> {
                       String inbound = tuple.getT1();
                       String outbound = tuple.getT2();
                       String transactionCode = TransactionCodeResolver.resolveTransactionCode(this);
                       CustomerEvent event = customerMapper.toEvent(saved, headers, inbound, outbound, transactionCode);

                       return eventPublisher.publishCreateEvent(event, headers)
                               .thenReturn(response);
                   }).onErrorResume(e->{
                       log.error("Error al serializar o enviar evento", e);
                       return Mono.just(response);
                   });
                });
    }

}
