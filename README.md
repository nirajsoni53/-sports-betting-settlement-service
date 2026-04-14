# Sports Betting Settlement Service

## Overview
This project simulates sports betting event outcome handling and bet settlement using Kafka and RocketMQ (mocked).

## Tech Stack
- Java 17
- Spring Boot
- Kafka
- H2 Database (In-memory)
- RocketMQ (Mocked)

## Prerequisites
Before you begin, ensure you have the following installed on your system:
- **Docker & Docker Compose**: Required to run the Kafka container. Verify your installation by running `docker --version` and `docker-compose --version`. If not installed, download and install it from the [official Docker website](https://docs.docker.com/get-docker/).
- **Java 17**: Required to run the Spring Boot application. Verify by running `java -version`.
- **Maven**: Required to build and run the application. Verify by running `mvn -version`.

## Flow
1. API publishes event outcome to Kafka topic `event-outcomes`
2. Kafka consumer listens and fetches matching bets from DB
3. Bets are evaluated (WON / LOST)
4. Settlement messages are sent to RocketMQ (mocked via logs)
5. Consumer processes settlement

## How to Run

### Step 1: Start Kafka (Docker)
Ensure the Docker daemon is running on your machine, then execute the following command in the project root directory:
```bash
docker-compose up -d
```

### Step 2: Run Application
Run the Spring Boot application using Maven:
```bash
mvn spring-boot:run
```

### Step 3: Test API

#### Option A: Using Postman (Recommended)
A Postman collection is included in the repository for easy testing.
1. Here Provided Postman JSON as `postman_collection.json` in the project root.
2. Open Postman and click **Import**.
3. Select the `postman_collection.json` file.
4. You will now have two pre-configured requests to test outcomes for either "Team A" or "Team B" winning.

#### Option B: Manual Testing
You can also test the API manually using cURL or any other REST client.

**Endpoint:** `POST http://localhost:8080/events/outcome`

**Request Body:**
```json
{
  "eventId": "event1",
  "eventName": "Match 1",
  "winnerId": "teamA"
}
```

## H2 Console
- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:testdb`

### Design Decision: Mocking RocketMQ
The assignment instructions stated: *"If the RocketMQ setup is too complex, use mocks for the RocketMQ producer."*

To mock the message queue locally without requiring a Dockerized RocketMQ instance, I utilized Spring's built-in **`ApplicationEventPublisher`** and **`@EventListener`**.
- This acts as an in-memory Pub/Sub broker.
- It perfectly simulates the decoupled, asynchronous nature of a real message queue (Producer -> Broker -> Consumer).
- If a real RocketMQ instance were to be added later, the application's architectural flow remains completely identical; only the publishing method needs to be swapped.

## Notes
- RocketMQ is mocked as per assignment instructions.
- Focus is on event-driven flow and system design.