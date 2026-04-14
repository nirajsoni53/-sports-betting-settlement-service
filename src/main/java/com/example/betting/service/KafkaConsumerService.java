package com.example.betting.service;

import com.example.betting.model.Bet;
import com.example.betting.model.EventOutcome;
import com.example.betting.repository.BetRepository;
import com.example.betting.rocketmq.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final BetRepository betRepository;
    private final RocketMQProducer rocketMQProducer;

    @KafkaListener(topics = "event-outcomes", groupId = "bet-group")
    public void consume(EventOutcome event) {
        if (event == null) {
            log.warn("Received null event outcome payload. Skipping processing.");
            return;
        }

        if (event.getEventId() == null || event.getWinnerId() == null) {
            log.warn("Received corrupted event outcome missing ID or Winner: {}. Skipping.", event);
            return;
        }

        log.info("Kafka Consumer Received Event: {}", event);

        List<Bet> bets = betRepository.findByEventId(event.getEventId());

        for (Bet bet : bets) {

            if (bet.getEventWinnerId().equals(event.getWinnerId())) {
                bet.setStatus("WON");
            } else {
                bet.setStatus("LOST");
            }

            rocketMQProducer.sendSettlement(bet);
        }
    }
}