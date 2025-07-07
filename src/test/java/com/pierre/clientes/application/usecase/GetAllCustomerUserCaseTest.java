package com.pierre.clientes.application.usecase;

import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.application.mapper.CustomerMapper;
import com.pierre.clientes.application.usecase.implementation.GetAllCustomerUseCaseImpl;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllCustomerUserCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private GetAllCustomerUseCaseImpl getAllCustomerUseCase;

    @Test
    void shouldReturnAllCustomers(){
        Customer customer1 = new Customer("1", "Pierre", "Castillo", "Llontop", LocalDateTime.now(), true);
        Customer customer2 = new Customer("2", "Abdiel", "Castillo", "Quiñonez", LocalDateTime.now(), true);

        CustomerResponseDTO dto1 = new CustomerResponseDTO("1", "Pierre Castillo Llontop");
        CustomerResponseDTO dto2 = new CustomerResponseDTO("2", "Abdiel Castillo Quiñonez");

        RequestHeaders headers = new RequestHeaders("consumerId", "traceparent", "deviceType", "deviceId");

        when(customerRepository.findAll()).thenReturn(Flux.just(customer1,customer2));
        when(customerMapper.toResponse(customer1)).thenReturn(dto1);
        when(customerMapper.toResponse(customer2)).thenReturn(dto2);

        StepVerifier.create(getAllCustomerUseCase.getAll(headers))
                .expectNext(dto1)
                .expectNext(dto2)
                .verifyComplete();

        verify(customerRepository).findAll();
        verify(customerMapper).toResponse(customer1);
        verify(customerMapper).toResponse(customer2);
    }
}
