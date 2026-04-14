package com.example.betting.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventOutcome {
    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "Event Name is required")
    private String eventName;

    @NotBlank(message = "Winner ID is required")
    private String winnerId;
}