package com.learning2.domains.configurations.kafka.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning2.apps.dtos.EntityTest;
import com.learning2.apps.dtos.EventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {

    @KafkaListener(topics = "${spring.kafka.consumer.template.default-topic}")
    @Retryable(value = {Exception.class}, maxAttempts = 10, // default 3
            backoff = @Backoff(delay = 10))
    public void consumer(@Payload(required = false) String value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        EventDTO event = mapper.readValue(value, EventDTO.class);
        System.out.println(event.getObject().toString());
    }
}
