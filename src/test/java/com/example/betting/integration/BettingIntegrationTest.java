package com.example.betting.integration;

import com.example.betting.model.Bet;
import com.example.betting.model.EventOutcome;
import com.example.betting.repository.BetRepository;
import com.example.betting.service.KafkaConsumerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class BettingIntegrationTest {

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    // We ONLY mock KafkaTemplate because we don't want to actually connect to Docker Kafka during the test
    @MockBean
    private KafkaTemplate<String, EventOutcome> kafkaTemplate;

    @Test
    void shouldProcessEndToEndFlow() throws InterruptedException {

        betRepository.deleteAll();

        Bet bet = new Bet("1", "user1", "event1",
                "market1", "teamA", 100.0, "PENDING");
        betRepository.save(bet);

        // Create an event where teamA wins
        EventOutcome event = new EventOutcome("event1", "Match 1", "teamA");

        // 2. Act: Manually trigger the Kafka consumer (simulating a message arriving from Kafka)
        kafkaConsumerService.consume(event);

        Thread.sleep(500);

        // 3. Assert: Verify the database was updated by the RocketMQConsumer
        List<Bet> updatedBets = betRepository.findByEventId("event1");
        assertFalse(updatedBets.isEmpty(), "Bet should exist in the database");

        Bet updatedBet = updatedBets.get(0);
        assertEquals("WON", updatedBet.getStatus(), "Bet status should have been updated to WON");
    }
}