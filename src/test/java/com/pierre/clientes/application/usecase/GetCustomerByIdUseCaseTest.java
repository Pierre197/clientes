package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.implementation.GetCustomerByIdUseCaseImpl;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCustomerByIdUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private GetCustomerByIdUseCaseImpl getCustomerByIdUseCase;

    @Test
    void shouldReturnCustomerById() {
        // ----- Arrange -----
        String id = UUID.randomUUID().toString();
        LocalDate birthdate = LocalDate.of(1994, 7, 14);
        int age = 30;
        LocalDateTime createdAt = LocalDateTime.now();

        Customer customer = new Customer(
                id, "Pierre", "Castillo", "Llontop",
                age, birthdate, createdAt, true
        );

        CustomerResponseDTO responseDTO = new CustomerResponseDTO(
                id,
                "Pierre Castillo Llontop",
                age,
                birthdate,
                birthdate.plusYears(80)
        );

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));
        when(customerMapper.toResponse(customer)).thenReturn(responseDTO);

        // ----- Act & Assert -----
        StepVerifier.create(getCustomerByIdUseCase.execute(id))
                .expectNext(responseDTO)
                .verifyComplete();

        verify(customerRepository).findById(id);
        verify(customerMapper).toResponse(customer);
    }
}
