package com.example.betting.controller;

import com.example.betting.model.EventOutcome;
import com.example.betting.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/outcome")
    public ResponseEntity<String> publish(@Valid @RequestBody EventOutcome event) {
        kafkaProducerService.sendEvent(event);
        return ResponseEntity.ok("Event sent to Kafka");
    }
}