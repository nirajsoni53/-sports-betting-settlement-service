package com.example.betting.rocketmq;

import com.example.betting.model.Bet;
import com.example.betting.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RocketMQConsumer {

    private final BetRepository betRepository;

    // Requirement: "listen to bet-settlements... consume the messages and settle the bets"
    @EventListener
    public void consume(Bet bet) {
        if (bet == null) {
            log.warn("Mock RocketMQ Consumer - Received null bet. Skipping settlement.");
            return;
        }

        log.info("Mock RocketMQ Consumer - Received message: {}", bet);

        // Settling the Bet in DB
        betRepository.save(bet);

        log.info("Bet {} successfully settled with status: {}", bet.getBetId(), bet.getStatus());
    }
}