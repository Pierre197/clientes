package com.pierre.clientes.infrastructure.persistence;

import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.repository.CustomerRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class MongoCustomerRepository implements CustomerRepository {

    private final SpringDataMongoCustomerRepository springDataRepository;

    public MongoCustomerRepository(SpringDataMongoCustomerRepository springDataMongoCustomerRepository){
        this.springDataRepository = springDataMongoCustomerRepository;
    }

    @Override
    public Mono<Customer> save(Customer customer){
        CustomerDocument doc = new CustomerDocument(
                customer.getId() != null ? customer.getId() : UUID.randomUUID().toString(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getSecondLastName(),
                customer.getCreatedAt() != null ? customer.getCreatedAt() : LocalDateTime.now(),
                customer.isActive()
        );

        return springDataRepository.save(doc)
                .map(saved -> new Customer(
                        saved.getId(),
                        saved.getFirstName(),
                        saved.getLastName(),
                        saved.getSecondLastName(),
                        saved.getCreatedAt(),
                        saved.isActive()
                ));
    }

    @Override
    public Mono<Customer> findById(String id){
        return springDataRepository.findById(id)
                .map(doc -> new Customer(
                        doc.getId(),
                        doc.getFirstName(),
                        doc.getLastName(),
                        doc.getSecondLastName(),
                        doc.getCreatedAt(),
                        doc.isActive()
                ));
    }

    @Override
    public Flux<Customer> findAll(){
        return springDataRepository.findAll()
                .map(doc -> new Customer(
                        doc.getId(),
                        doc.getFirstName(),
                        doc.getLastName(),
                        doc.getSecondLastName(),
                        doc.getCreatedAt(),
                        doc.isActive()
                ));
    }
}
