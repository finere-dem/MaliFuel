# Fuel Queue Management System - Bamako

Spring Boot 3.2 backend that manages fuel station queues for Bamako. The platform supports JWT-secured APIs for three key personas:

- **USAGER** – register, manage vehicle/localities, join queue, track ticket
- **GERANT** – monitor station queues in real-time, validate/serve tickets
- **ADMIN** – manage users, localities, stations, and platform analytics

## Tech Stack

- Java 17, Spring Boot 3.2.5, Maven
- Spring Web, Data JPA (PostgreSQL), Security (JWT), Validation
- MapStruct & Lombok for DTO mapping and boilerplate reduction
- ZXing for QR code generation (Base64 PNG)
- Spring WebSocket (STOMP) for queue updates (`/topic/stations/{stationId}/queue`)
- SpringDoc OpenAPI (`/swagger-ui.html`) for API docs

## Getting Started

1. **Configure PostgreSQL**
   ```bash
   createdb fuel_queue
   ```
2. **Set environment variables (optional)**
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:5432/fuel_queue
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=postgres
   export JWT_SECRET=super-secret
   export JWT_EXPIRATION_MINUTES=120
   ```
3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
4. **Explore APIs**
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - WebSocket endpoint: `ws://localhost:8080/ws`

## Key Endpoints

| Area | Endpoint | Description |
|------|----------|-------------|
| Auth | `POST /api/v1/auth/register` | Register user + vehicle + localities |
| Auth | `POST /api/v1/auth/login` | Obtain JWT |
| User | `GET /api/v1/users/me` | Profile |
| User | `PUT /api/v1/users/me` | Update names/vehicle |
| User | `POST /api/v1/users/me/localities` | Add locality (max 2, primary flag) |
| Station | `GET /api/v1/localities` | List localities |
| Station | `GET /api/v1/localities/{id}/stations` | Filter by fuel type |
| Ticket | `POST /api/v1/tickets` | Join queue (returns position + QR) |
| Ticket | `DELETE /api/v1/tickets/{id}` | Cancel queue ticket |
| Gérant | `GET /api/v1/stations/{id}/tickets` | Waiting queue overview |
| Gérant | `POST /api/v1/stations/{id}/tickets/{ticketId}/scan-validate` | Serve ticket + record consumption |
| Admin | `POST /api/v1/admin/stations` | Manage stations |
| Admin | `GET /api/v1/admin/analytics/consumption` | Date-range analytics |

## Project Structure

```
src/main/java/com/bamako/fuelqueue
├── FuelQueueManagementApplication.java
├── config            # JPA auditing & OpenAPI configuration
├── controller        # REST controllers per bounded context
├── domain            # Entities, enums, repositories, mappers
├── dto               # Request & response payloads
├── exception         # Custom exceptions + global handler
├── security          # JWT properties, filters, security config
├── service           # Service interfaces + implementations
└── websocket         # STOMP WebSocket configuration
```

## Notes

- Auditing (`createdAt`/`updatedAt`) enabled on all entities.
- JWT secret and database credentials should be externalised for production.
- WebSocket payload is a simple `{"event":"QUEUE_UPDATED","stationId":"..."}` message; clients are expected to fetch updated queue snapshots via REST.
- Ready for future enhancements such as Redis-backed queue caching or station-manager assignments.

## Testing

The project currently ships without automated tests. Suggested next steps:

- Add unit tests for the service layer (queueing logic, analytics).
- Add integration tests leveraging Testcontainers for PostgreSQL.
- Mock `SimpMessagingTemplate` when testing queue notifications.
