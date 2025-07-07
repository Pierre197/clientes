package com.pierre.clientes.infrastructure.persistence;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerDocument {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String secondLastName;
    private LocalDateTime createdAt;
    private boolean active;
}
