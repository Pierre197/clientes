package com.pierre.clientes.infrastructure.persistence;

import com.pierre.clientes.domain.model.Customer;
import com.pierre.clientes.domain.repository.CustomerRepository;
import com.pierre.clientes.infrastructure.persistence.mapper.CustomerDocumentMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MongoCustomerRepository implements CustomerRepository {

    private final SpringDataMongoCustomerRepository springDataRepository;

    public MongoCustomerRepository(SpringDataMongoCustomerRepository springDataMongoCustomerRepository){
        this.springDataRepository = springDataMongoCustomerRepository;
    }

    @Override
    public Mono<Customer> save(Customer customer){
        return springDataRepository.save(CustomerDocumentMapper.toDocument(customer))
                .map(CustomerDocumentMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(String id){
        return springDataRepository.findById(id)
                .map(CustomerDocumentMapper::toDomain);
    }

    @Override
    public Flux<Customer> findAll(){
        return springDataRepository.findAll()
                .map(CustomerDocumentMapper::toDomain);
    }
}
