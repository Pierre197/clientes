package com.pierre.clientes.infrastructure.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface SpringDataMongoCustomerRepository extends ReactiveMongoRepository<CustomerDocument, String> {

    Flux<CustomerDocument> findAll();
}
