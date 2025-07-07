package com.pierre.clientes.domain.exception;

public class InvalidCustomerDataException extends RuntimeException{
    public InvalidCustomerDataException(String message){
        super(message);
    }
}
