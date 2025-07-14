package com.pierre.clientes.infrastructure.persistence.mapper;

import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.infrastructure.persistence.CustomerDocument;

public class CustomerDocumentMapper {

    public static CustomerDocument toDocument(Customer customer) {
        return new CustomerDocument(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSecondLastName(),
                customer.getAge(),
                customer.getBirthdate(),
                customer.getCreatedAt(),
                customer.isActive()
        );
    }

    public static Customer toDomain(CustomerDocument doc) {
        return new Customer(
                doc.getId(),
                doc.getFirstName(),
                doc.getLastName(),
                doc.getSecondLastName(),
                doc.getAge(),
                doc.getBirthdate(),
                doc.getCreatedAt(),
                doc.isActive()
        );
    }
}
