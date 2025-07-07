package com.pierre.clientes.infrastructure.messaging.kafka.publisher;

import com.pierre.clientes.domain.event.CustomerEvent;
import com.pierre.clientes.domain.model.shared.RequestHeaders;
import com.pierre.clientes.infrastructure.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveKafkaEventPublisher {
    private final KafkaSender<String, CustomerEvent> kafkaSender;

    @Value("${topic.customer.created}")
    private String createdTopic;

    @Value("${topic.customer.consulted}")
    private String consultedTopic;

    public Mono<SenderResult<Void>> publishCreateEvent(CustomerEvent event, RequestHeaders headers){
        return sendEvent(createdTopic, event, headers);
    }

    public Mono<SenderResult<Void>> publishConsultEvent(CustomerEvent event, RequestHeaders headers){
        return sendEvent(consultedTopic, event, headers);
    }

    public Mono<SenderResult<Void>> sendEvent(String topic, CustomerEvent event, RequestHeaders headers){
        ProducerRecord<String, CustomerEvent> producerRecord = new ProducerRecord<>(topic, event.customerId(), event);

        producerRecord.headers().add("consumerId", headers.consumerId().getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("traceparent", headers.traceparent().getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("deciceType", headers.deviceType().getBytes(StandardCharsets.UTF_8));
        producerRecord.headers().add("deviceId", headers.deviceId().getBytes(StandardCharsets.UTF_8));

        String payload = JsonUtils.safeWriteASString(event);
        log.info("Trama enviada a Kafka: {}", payload);

        SenderRecord<String, CustomerEvent, Void> senderRecord = SenderRecord.create(producerRecord,null);

        return kafkaSender.send(Mono.just(senderRecord))
                .doOnNext(result -> log.info("Evento publicado: topic={}, offset={}", result.recordMetadata().topic(), result.recordMetadata().offset()))
                .doOnError(e -> log.error("Error al enviar evento a Kafka", e))
                .next();
    }



}
