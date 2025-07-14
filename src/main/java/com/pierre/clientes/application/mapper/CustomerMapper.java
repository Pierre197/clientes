package com.pierre.clientes.application.mapper;

import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.application.dto.CustomerRequestDTO;
import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.infrastructure.util.TimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    default Customer toDomain(CustomerRequestDTO dto){
        return Customer.create(
                dto.firstName(),
                dto.lastName(),
                dto.secondLastName(),
                dto.age(),
                dto.birthdate()
        );
    }

    default CustomerResponseDTO toResponse(Customer customer) {

        LocalDate birthdate = customer.getBirthdate();
        LocalDate estimatedDeathDate = birthdate != null
                ? birthdate.plusYears(80)
                : null; // o LocalDate.of(1900, 1, 1)

        return new CustomerResponseDTO(
                customer.getId(),
                buildFullName(customer),
                customer.getAge(),
                customer.getBirthdate(),
                estimatedDeathDate
        );
    }

    default String buildFullName(Customer customer){
        return  String.join(" ",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSecondLastName()).trim();
    }

    // Evento con Customer (creación)
    default CustomerEvent toEvent(Customer customer, RequestHeaders headers, String inboundJson, String outboundJson, String transactionCode) {
        return new CustomerEvent(
                "clientes-service",                     // applicationId
                headers.consumerId(),                   // consumerId
                customer.getId(),                       // customerId
                TimeUtils.currentTimestamp(),
                headers.traceparent(),                  // traceId
                inboundJson,
                outboundJson,
                transactionCode
        );
    }

    // Evento SIN Customer (Consulta)
    default CustomerEvent toEvent(RequestHeaders headers, String inboundJson, String outboundJson, String transactionCode){
        return new CustomerEvent(
                "clientes-service",
                headers.consumerId(),
                null,                                     // No customerId
                TimeUtils.currentTimestamp(),
                headers.traceparent(),
                inboundJson,
                outboundJson,
                transactionCode
        );
    }
}
