package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.implementation.GetAllCustomerUseCaseImpl;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.domain.repository.CustomerRepository;
import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.infrastructure.messaging.kafka.publisher.ReactiveKafkaEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAllCustomerUserCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private ReactiveKafkaEventPublisher eventPublisher;

    @InjectMocks
    private GetAllCustomerUseCaseImpl getAllCustomerUseCase;

    @Test
    void shouldReturnAllCustomers() {
        // ----- Arrange -----
        Customer customer1 = new Customer(
                UUID.randomUUID().toString(),
                "Pierre", "Castillo", "Llontop",
                30,
                LocalDate.of(1994, 7, 14),
                LocalDateTime.now(),
                true
        );

        Customer customer2 = new Customer(
                UUID.randomUUID().toString(),
                "Abdiel", "Castillo", "Quiñonez",
                11,
                LocalDate.of(2013, 5, 20),
                LocalDateTime.now(),
                true
        );

        CustomerResponseDTO dto1 = new CustomerResponseDTO(
                customer1.getId(),
                "Pierre Castillo Llontop",
                30,
                LocalDate.of(1994, 7, 14),
                LocalDate.of(2074, 7, 14)
        );

        CustomerResponseDTO dto2 = new CustomerResponseDTO(
                customer2.getId(),
                "Abdiel Castillo Quiñonez",
                11,
                LocalDate.of(2013, 5, 20),
                LocalDate.of(2083, 5, 20)
        );

        RequestHeaders headers = new RequestHeaders("consumerId", "traceparent");

        when(customerRepository.findAll()).thenReturn(Flux.just(customer1, customer2));
        when(customerMapper.toResponse(customer1)).thenReturn(dto1);
        when(customerMapper.toResponse(customer2)).thenReturn(dto2);
        when(customerMapper.toEvent(any(), any(), any(), any()))
                .thenReturn(mock(CustomerEvent.class));
        when(eventPublisher.publishConsultEvent(any(), any())).thenReturn(Mono.empty());

        // ----- Act & Assert -----
        StepVerifier.create(getAllCustomerUseCase.getAll(headers))
                .expectNext(dto1)
                .expectNext(dto2)
                .verifyComplete();

        verify(customerRepository).findAll();
        verify(customerMapper).toResponse(customer1);
        verify(customerMapper).toResponse(customer2);
        verify(eventPublisher).publishConsultEvent(any(), eq(headers));
    }
}
