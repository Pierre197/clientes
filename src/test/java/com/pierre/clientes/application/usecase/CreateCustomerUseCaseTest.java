package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerRequestDTO;
import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.implementation.CreateCustomerUseCaseImpl;
import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.domain.repository.CustomerRepository;
import com.pierre.clientes.infrastructure.messaging.kafka.publisher.ReactiveKafkaEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @Mock
    ReactiveKafkaEventPublisher eventPublisher;

    @InjectMocks
    CreateCustomerUseCaseImpl createCustomerUseCase;

    @Test
    void shouldCreateCustomerSuccessfully() {
        // ----- Arrange -----
        CustomerRequestDTO request =
                new CustomerRequestDTO("Pierre", "Castillo", "Llontop", 30, LocalDate.of(1994, 7, 14));

        Customer domainCustomer = mock(Customer.class); // evitamos constructor real
        CustomerResponseDTO response = new CustomerResponseDTO(
                "1",
                "Pierre Castillo Llontop",
                30,
                LocalDate.of(1994, 7, 19),
                LocalDate.of(2074, 7, 19)
        );
        RequestHeaders headers = new RequestHeaders("consumerId", "traceparent");

        CustomerEvent event = mock(CustomerEvent.class);

        when(customerMapper.toDomain(request)).thenReturn(domainCustomer);
        when(customerRepository.save(domainCustomer)).thenReturn(Mono.just(domainCustomer));
        when(customerMapper.toResponse(domainCustomer)).thenReturn(response);
        when(customerMapper.toEvent(eq(domainCustomer), eq(headers), anyString(), anyString(), anyString()))
                .thenReturn(event);
        when(eventPublisher.publishCreateEvent(eq(event), eq(headers))).thenReturn(Mono.empty());

        // ----- Act -----
        Mono<CustomerResponseDTO> result = createCustomerUseCase.execute(request, headers);

        // ----- Assert -----
        StepVerifier.create(result)
                .expectNext(response)
                .verifyComplete();

        verify(customerMapper).toDomain(request);
        verify(customerRepository).save(domainCustomer);
        verify(customerMapper).toResponse(domainCustomer);
        verify(customerMapper).toEvent(eq(domainCustomer), eq(headers), anyString(), anyString(), anyString());
        verify(eventPublisher).publishCreateEvent(eq(event), eq(headers));
    }
}
