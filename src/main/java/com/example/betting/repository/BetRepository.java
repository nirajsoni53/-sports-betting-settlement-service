package com.example.betting.repository;

import com.example.betting.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BetRepository extends JpaRepository<Bet, String> {
    List<Bet> findByEventId(String eventId);
}