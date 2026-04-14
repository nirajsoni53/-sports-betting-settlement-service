package com.example.betting.service;

import com.example.betting.model.Bet;
import com.example.betting.model.EventOutcome;
import com.example.betting.repository.BetRepository;

import com.example.betting.rocketmq.RocketMQProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private RocketMQProducer rocketMQProducer;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void shouldProcessEventAndSettleBets() {

        EventOutcome event = new EventOutcome("event1", "Match 1", "teamA");

        Bet bet1 = new Bet("1", "user1", "event1", "market1", "teamA", 100.0, "PENDING");
        Bet bet2 = new Bet("2", "user2", "event1", "market1", "teamB", 200.0, "PENDING");

        when(betRepository.findByEventId("event1"))
                .thenReturn(List.of(bet1, bet2));

        kafkaConsumerService.consume(event);

        assertEquals("WON", bet1.getStatus());
        assertEquals("LOST", bet2.getStatus());

        verify(rocketMQProducer, times(2))
                .sendSettlement(any(Bet.class));
    }

    @Test
    void shouldHandleNoBetsFound() {

        EventOutcome event = new EventOutcome("event1", "Match 1", "teamA");

        when(betRepository.findByEventId("event1"))
                .thenReturn(Collections.emptyList());

        kafkaConsumerService.consume(event);

        verify(rocketMQProducer, never())
                .sendSettlement(any(Bet.class));
    }

    @Test
    void shouldHandleNullEventGracefully() {
        kafkaConsumerService.consume(null);

        verify(betRepository, never()).findByEventId(anyString());
        verify(rocketMQProducer, never()).sendSettlement(any(Bet.class));
    }

    @Test
    void shouldHandleEventWithNullWinnerGracefully() {
        EventOutcome invalidEvent = new EventOutcome("event1", "Match 1", null);

        kafkaConsumerService.consume(invalidEvent);

        verify(betRepository, never()).findByEventId(anyString());
        verify(rocketMQProducer, never()).sendSettlement(any(Bet.class));
    }
}