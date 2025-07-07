package com.pierre.clientes.domain.model;

import com.pierre.clientes.domain.exception.InvalidCustomerDataException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String secondLastName;
    private LocalDateTime createdAt;
    private boolean active;


    public Customer(String firstName, String lastName, String secondLastName){
        if (firstName == null || firstName.trim().isEmpty()){
            throw new InvalidCustomerDataException("El nombre no puede ser nulo ni vacío");
        }
        if (lastName == null || lastName.trim().isEmpty()){
            throw new InvalidCustomerDataException("El apellido paterno no puede ser nulo ni vacío");
        }
        if (secondLastName == null){
            throw new InvalidCustomerDataException("El apellido materno no puede ser nulo");
        }

        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    /**
     * Constructor usado solo para infraestructura (ej. al reconstruir desde la base de datos)
     * @param id
     * @param firstName
     * @param lastName
     * @param secondLastName
     * @param createdAt
     * @param active
     */
    public Customer(String id, String firstName, String lastName, String secondLastName, LocalDateTime createdAt, boolean active){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.secondLastName = secondLastName;
        this.createdAt = createdAt;
        this.active = active;
    }

    public String getId(){
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getSecondLastName(){
        return secondLastName;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public boolean isActive(){
        return active;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setSecondLastName(String secondLastName){
        this.secondLastName = secondLastName;
    }

    public void deactivate(){
        this.active = false;
    }

    public void activate(){
        this.active = true;
    }
}
