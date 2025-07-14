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

                   //Serializar request y response como strings para evento
                    String inbound = JsonUtils.toJson(dto);
                    String outbound = JsonUtils.toJson(response);
                    String transactionCode = TransactionCodeResolver.resolveTransactionCode(this);

                    //Generar evento generico CustomerEvent
                    CustomerEvent event = customerMapper.toEvent(saved, headers, inbound, outbound, transactionCode);

                    //Enviar a Kafka, y retornar response incluso si falla el publish
                    return eventPublisher .publishCreateEvent(event, headers)
                            .thenReturn(response)
                            .onErrorResume(e -> {
                                log.error("Error al enviar evento a kafka", e);
                                return Mono.just(response);
                            });
                });
    }

}
