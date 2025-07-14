package com.pierre.clientes.infrastructure.config.openapi;

import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiHeaderConfig {

    @Bean
    public OpenApiCustomizer customerHeaderCustomizer(){
        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            if (path.equals("/auth/login")){
                return;
            }

            pathItem.readOperations().forEach(operation -> {
                operation.addParametersItem(new HeaderParameter()
                        .name("consumerId")
                        .description("Identificador del consumidor")
                        .required(true)
                        .schema(new StringSchema()));

                operation.addParametersItem(new HeaderParameter()
                        .name("traceparent")
                        .description("ID de trazabilidad (formato W3C Trace Context)")
                        .required(true)
                        .schema(new StringSchema()));
            });
        });
    }
}