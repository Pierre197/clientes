package com.pierre.clientes.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static String safeWriteASString(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error serializando JSON", e);
        }
    }
}
