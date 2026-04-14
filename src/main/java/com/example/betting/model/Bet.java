package com.example.betting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bet {

    @Id
    private String betId;
    private String userId;
    private String eventId;
    private String eventMarketId;
    private String eventWinnerId;
    private Double betAmount;
    private String status;
}