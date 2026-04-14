package com.example.betting.rocketmq;

import com.example.betting.model.Bet;

import com.example.betting.repository.BetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RocketMQConsumerTest {

    @Mock
    private BetRepository betRepository;

    @InjectMocks
    private RocketMQConsumer rocketMQConsumer;

    @Test
    void shouldConsumeSettlementSuccessfully() {

        Bet bet = new Bet("1", "user1", "event1",
                "market1", "teamA", 100.0, "WON");

        assertDoesNotThrow(() -> {
            rocketMQConsumer.consume(bet);
        });

        verify(betRepository, times(1)).save(bet);
    }

    @Test
    void shouldThrowExceptionOnNullBet() {
        assertThrows(NullPointerException.class, () -> {
            rocketMQConsumer.consume(null);
        });

        verify(betRepository, times(1)).save(null);
    }
}