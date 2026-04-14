package com.example.betting;

import com.example.betting.model.Bet;
import com.example.betting.repository.BetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BettingApplication {
	public static void main(String[] args) {
		SpringApplication.run(BettingApplication.class, args);
	}

	@Bean
	CommandLineRunner init(BetRepository repo) {
		return args -> {
			repo.save(new Bet("1","user1","event1","m1","teamA",100.0,"PENDING"));
			repo.save(new Bet("2","user2","event1","m1","teamB",200.0,"PENDING"));
		};
	}
}
