package com.pierre.clientes.application.mapper;

import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.application.dto.CustomerRequestDTO;
import com.pierre.clientes.application.dto.CustomerResponseDTO;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    default Customer toDomain(CustomerRequestDTO dto){
        return new Customer(
                dto.firstName(),
                dto.lastName(),
                dto.secondLastName()
        );
    }

    @Mapping(target = "fullName", expression = "java(buildFullName(customer))")
    CustomerResponseDTO toResponse(Customer customer);

    default String buildFullName(Customer customer){
        return  String.join(" ",
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSecondLastName()).trim();
    }

    // Evento con Customer (creación)
    default CustomerEvent toEvent(Customer customer, RequestHeaders headers, String inboundJson, String outboundJson, String transactionCode){
        return new CustomerEvent(
                "application-" + headers.consumerId(),
                headers.consumerId(),
                String.valueOf(System.currentTimeMillis()),
                headers.consumerId(),
                OffsetDateTime.now(ZoneOffset.of("-05:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")),
                customer.getId(),
                "default-region",
                "0000",
                String.valueOf(Instant.now().getEpochSecond()),
                headers.traceparent(),
                inboundJson,
                outboundJson,
                transactionCode
        );
    }

    // Evento SIN Customer (Consulta)
    default CustomerEvent toEvent(RequestHeaders headers, String inboundJson, String outboundJson, String transactionCode){
        return new CustomerEvent(
                "application-" + headers.consumerId(),
                headers.consumerId(),
                String.valueOf(System.currentTimeMillis()),
                headers.consumerId(),
                OffsetDateTime.now(ZoneOffset.of("-05:00")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")),
                null, // customerId no aplica en consulta general
                "default-region",
                "0000",
                String.valueOf(Instant.now().getEpochSecond()),
                headers.traceparent(),
                inboundJson,
                outboundJson,
                transactionCode
        );
    }
}
