package com.pierre.clientes.domain.model;

import com.pierre.clientes.domain.exception.InvalidCustomerDataException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;


public class Customer {

    private final String id;
    private String firstName;
    private String lastName;
    private String secondLastName;
    private Integer age;
    private LocalDate birthdate;
    private final LocalDateTime createdAt;
    private boolean active;

    // ---------- Factory ----------
    public static Customer create(
            String firstName,
            String lastName,
            String secondLastName,
            Integer age,            // puede venir null
            LocalDate birthdate     // puede venir null
    ) {
        // Reglas mínimas de obligatoriedad
        requireNonBlank(firstName,  "El nombre no puede ser nulo ni vacío");
        requireNonBlank(lastName,   "El apellido paterno no puede ser nulo ni vacío");
        if (secondLastName == null) {
            throw new InvalidCustomerDataException("El apellido materno no puede ser nulo");
        }

        // Regla de coherencia entre edad y fecha
        if (age == null && birthdate == null) {
            throw new InvalidCustomerDataException(
                    "Debe especificarse al menos edad o fecha de nacimiento");
        }

        if (birthdate != null && birthdate.isAfter(LocalDate.now())) {
            throw new InvalidCustomerDataException(
                    "La fecha de nacimiento no puede estar en el futuro");
        }

        // Si solo llega birthdate ► calcular edad
        if (age == null) {
            age = calculateAge(birthdate);
        }

        // Si llegan ambos ► validar coherencia
        if (birthdate != null) {
            int calculated = calculateAge(birthdate);
            int difference = Math.abs(calculated - age);
            if (difference > 1) {   // margen de 1 año por meses/días aún no cumplidos
                throw new InvalidCustomerDataException(
                        "Edad ("+age+") no coincide con la fecha de nacimiento ("+birthdate+")");
            }
            age = calculated; // normalizamos para que siempre sea consistente
        }

        // Construcción segura
        return new Customer(
                UUID.randomUUID().toString(),
                firstName,
                lastName,
                secondLastName,
                age,
                birthdate,
                LocalDateTime.now(),
                true
        );
    }

    /** Constructor sólo para reconstrucción desde persistencia
     * No aplicar validaciones de negocio aqui
     * */
    public Customer(
            String id,
            String firstName,
            String lastName,
            String secondLastName,
            Integer age,
            LocalDate birthdate,
            LocalDateTime createdAt,
            boolean active) {

        this.id             = id;
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.secondLastName = secondLastName;
        this.age            = age;
        this.birthdate      = birthdate;
        this.createdAt      = createdAt;
        this.active         = active;
    }

    // ---------- Métodos de dominio ----------
    public void updateNames(String firstName, String lastName, String secondLastName) {
        requireNonBlank(firstName, "El nombre no puede ser nulo ni vacío");
        requireNonBlank(lastName,  "El apellido paterno no puede ser nulo ni vacío");
        if (secondLastName == null) {
            throw new InvalidCustomerDataException("El apellido materno no puede ser nulo");
        }
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.secondLastName = secondLastName;
    }

    public void deactivate() { this.active = false; }
    public void activate()   { this.active = true; }

    // ---------- Getters ----------
    public String getId()         { return id; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getSecondLastName() { return secondLastName; }
    public Integer getAge()       { return age; }
    public LocalDate getBirthdate(){ return birthdate; }
    public LocalDateTime getCreatedAt(){ return createdAt; }
    public boolean isActive()     { return active; }

    // ---------- Helpers privados ----------
    private static int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }

    private static void requireNonBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidCustomerDataException(message);
        }
    }
}
