package com.pierre.clientes.application.usecase.implementation;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.GetAllCustomerUseCase;
import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.domain.model.shared.TransactionCode;
import com.pierre.clientes.domain.model.shared.UseCaseTransactionCode;
import com.pierre.clientes.domain.repository.CustomerRepository;
import com.pierre.clientes.domain.util.TransactionCodeResolver;
import com.pierre.clientes.infrastructure.messaging.kafka.publisher.ReactiveKafkaEventPublisher;
import com.pierre.clientes.infrastructure.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
@UseCaseTransactionCode(TransactionCode.GET)
public class GetAllCustomerUseCaseImpl implements GetAllCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ReactiveKafkaEventPublisher eventPublisher;

    @Override
    public Flux<CustomerResponseDTO> getAll(RequestHeaders headers){
        return customerRepository.findAll()
                .map(customerMapper::toResponse)
                .collectList()
                .flatMapMany(list -> {
                    try {
                        String inbound = "{}";
                        String outbound = JsonUtils.safeWriteASString(list);
                        String transactionCode = TransactionCodeResolver.resolveTransactionCode(this);

                        CustomerEvent event = customerMapper.toEvent(headers, inbound, outbound, transactionCode);

                        return eventPublisher.publishConsultEvent(event, headers)
                                .thenMany(Flux.fromIterable(list));
                    }catch (Exception e){
                        log.error("Error al serializar o enviar evento de consulta", e);
                        return Flux.fromIterable(list);
                    }
                });
    }
}
