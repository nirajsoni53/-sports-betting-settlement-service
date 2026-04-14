package com.example.betting.service;

import com.example.betting.model.EventOutcome;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, EventOutcome> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    private EventOutcome event;

    @BeforeEach
    void setup() {
        event = new EventOutcome("event1", "Match 1", "teamA");
    }

    @Test
    void shouldSendEventToKafka() {
        kafkaProducerService.sendEvent(event);

        verify(kafkaTemplate, times(1))
                .send("event-outcomes", event);
    }

    @Test
    void shouldThrowExceptionWhenKafkaFails() {
        doThrow(new RuntimeException("Kafka failure"))
                .when(kafkaTemplate)
                .send(anyString(), any(EventOutcome.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            kafkaProducerService.sendEvent(event);
        });

        assertEquals("Kafka failure", ex.getMessage());

        verify(kafkaTemplate, times(1))
                .send("event-outcomes", event);
    }
}