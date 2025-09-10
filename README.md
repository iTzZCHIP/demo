
# ITZBund Service Desk – Demo (Spring Boot)

Kurze Demo-Anwendung im Kontext ITZBund: kleiner Servicekatalog und Ticket-Management. Ziel ist, typische Verwaltungsfälle realistisch, aber kompakt zu zeigen.

## Technologiestack
- Java 17+, Spring Boot 3.x (Web, Data JPA, Validation)
- H2 In-Memory DB, Spring Data JPA (Hibernate)
- DTOs als Java Records
- springdoc-openapi (Swagger UI)
- Logging (SLF4J) mit MDC Correlation-ID
- JPA Auditing (createdAt/updatedAt)

## Domänenmodell (klein)
- User (app_user)
- ServiceOffering (service_offering) mit Spalte `svc_key` (statt reserviertem `key`)
- Ticket (ticket)
  - Ticket → requester (User) [n:1]
  - Ticket → assignee (User, optional) [n:1]
  - Ticket → serviceOffering (ServiceOffering) [n:1]
  - Enums: TicketStatus, TicketPriority

## Cross-Cutting Concerns & Lösungsansätze
- Validierung: Hibernate Validator (@NotNull, @NotBlank, @Size) auf Record-DTOs
- Globales Fehlerhandling: `@ControllerAdvice` mit strukturiertem `ErrorResponse`
- Logging/Tracing: `CorrelationIdFilter` nutzt `X-Correlation-Id` + MDC (Logs enthalten corrId)
- Auditing: `@EnableJpaAuditing` + Basisklasse für `createdAt`/`updatedAt`
- API-Dokumentation: springdoc-openapi, Swagger UI (siehe unten)
- Versionierung: API-Präfix `/api/v1/` für künftige Erweiterbarkeit
- Pagination/Sorting: Spring Data `Pageable` in List-Endpunkten
- H2-Console: aktiviert für lokale Entwicklung (`/h2`)
- Optional erweiterbar: Security (Basic Auth), Micrometer/Prometheus, Caching, Optimistic Locking (`@Version`)

## Build & Run
Voraussetzungen: Java 17+, Maven oder Gradle

Starten:
- Maven: `mvn spring-boot:run`
- Gradle: `./gradlew bootRun`

H2: In-Memory (jdbc URL: `jdbc:h2:mem:itzbund_demo`), Console: http://localhost:8080/h2

Hinweise für `data.sql`:
- In `application.properties`: `spring.jpa.defer-datasource-initialization=true`
- UUIDs in `data.sql` mit einfachen Anführungszeichen schreiben (`'...'`)
- Spaltenname `svc_key` für ServiceOfferings verwenden

## API-Dokumentation (Swagger)
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

## Logging
Konfiguration (Beispiel):
```
logging.level.root=INFO
logging.level.de.paulwunsch.bund=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} %5p [corr:%X{corrId}] --- [%t] %logger{36} : %msg%n
```

## Tests
- Es gibt aktuell keine Integrationstests.
- Vorhanden: Unit-Tests für den Service (z. B. `TicketService`) mit gemockten Repositories.
- Ausführen: `mvn test` bzw. `./gradlew test`

