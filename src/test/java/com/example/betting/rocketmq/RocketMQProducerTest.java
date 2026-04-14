package com.example.betting.rocketmq;

import com.example.betting.model.Bet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RocketMQProducerTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RocketMQProducer rocketMQProducer;

    @Test
    void shouldSendSettlementAndInvokeConsumer() {

        Bet bet = new Bet("1", "user1", "event1",
                "market1", "teamA", 100.0, "PENDING");

        rocketMQProducer.sendSettlement(bet);

        verify(eventPublisher, times(1))
                .publishEvent(bet);
    }

    @Test
    void shouldHandleNullBetGracefully() {

        rocketMQProducer.sendSettlement(null);

        verify(eventPublisher, never())
                .publishEvent(any());
    }
}