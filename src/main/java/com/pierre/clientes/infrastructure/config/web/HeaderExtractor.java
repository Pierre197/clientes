package com.pierre.clientes.infrastructure.config.web;

import com.pierre.clientes.domain.model.shared.RequestHeaders;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

@Component
public class HeaderExtractor {
    public RequestHeaders extract(HttpHeaders headers){
        return  new RequestHeaders(
                headers.getFirst("consumerId"),
                headers.getFirst("traceparent")
        );
    }
}
