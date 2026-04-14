package com.example.betting.service;

import com.example.betting.model.EventOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, EventOutcome> kafkaTemplate;

    public void sendEvent(EventOutcome event) {
        log.info("Publishing event to Kafka: {}", event);
        kafkaTemplate.send("event-outcomes", event);
    }
}