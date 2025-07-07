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

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCustomerByIdUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private GetCustomerByIdUseCaseImpl getCustomerByIdUseCase;

    @Test
    void shouldReturnCustomerById(){
        String id = "1";
        Customer customer = new Customer(id, "Pierre", "Castillo", "Llontop", LocalDateTime.now(), true);
        CustomerResponseDTO responseDTO = new CustomerResponseDTO(id, "Pierre Castillo Llontop");

        when(customerRepository.findById(id)).thenReturn(Mono.just(customer));
        when(customerMapper.toResponse(customer)).thenReturn(responseDTO);

        StepVerifier.create(getCustomerByIdUseCase.execute(id))
                .expectNext(responseDTO)
                .verifyComplete();

        verify(customerRepository).findById(id);
        verify(customerMapper).toResponse(customer);
    }

}
