package com.example.betting.rocketmq;

import com.example.betting.model.Bet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RocketMQProducer {

    // Built-in Spring feature to publish events (acts as our Mock MQ)
    private final ApplicationEventPublisher eventPublisher;

    public void sendSettlement(Bet bet) {
        if (bet == null) {
            log.warn("Attempted to publish null bet. Skipping.");
            return;
        }
        
        log.info("Mock RocketMQ Producer - Sending payload to bet-settlements: {}", bet);
        eventPublisher.publishEvent(bet);
    }
}