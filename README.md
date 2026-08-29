<h1 align="center">Hotel Management System</h1>

<p align="center">
    <img height="25" src="https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white" />
    <img height="25" src="https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven&logoColor=white" />
    <img height="25" src="https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?logo=springboot&logoColor=white" />
    <img height="25" src="https://img.shields.io/badge/JaCoCo-0.8.15-3C873A" />
    <img height="25" src="https://img.shields.io/badge/License-MIT-yellow.svg" />
</p>

<p align="center">
    <a href="./docs/Doc-PT-BR.pdf">
        <img height="25"
             src="https://img.shields.io/badge/Documentação-PT--BR%20%7C%20Em%20Desenvolvimento-2E8B57?logo=readthedocs&logoColor=white" />
    </a>
    <a href="./docs/Doc-EN.pdf">
        <img height="25"
             src="https://img.shields.io/badge/Documentation-EN%20%7C%20In%20Development-F59E0B?logo=readthedocs&logoColor=white" />
    </a>
</p>

---

## What is Hotel Management?

Hotel Management is a backend REST API designed to support the core operations of a hotel chain.

The project was created as a practical software engineering environment for applying backend development concepts such as modular architecture, domain modeling, persistence, API design, validation, automated testing, external integrations, and security.

The system follows an incremental development approach, allowing new modules and business requirements to be introduced while preserving clear domain boundaries.

## Objective

The main objective is to design and implement a modular hotel management system capable of supporting operations related to people, employees, hotels, rooms, reservations, stays, and payments.

Beyond the functional requirements, the project also focuses on:

* Domain-driven modeling
* Modular boundaries
* Maintainable application architecture
* Explicit business rules
* Optimized database queries
* API contract separation through DTOs
* Automated testing
* Database versioning
* External service integrations
* Authentication and authorization

---

> ⚠️ **Project status:** This project is currently under active development.
>
> 🔐 **Security roadmap:** Authentication and authorization will be implemented using Spring Security and OAuth2, including role-based access control and protected API routes.

---

## Key Features

Current and planned capabilities include:

* Person and customer management
* Employee lifecycle management
* Job position management
* Hotel chain management
* Hotel amenities and classification
* Room and room type management
* Reservation management
* Stay and check-in/check-out management
* Payment processing
* Address resolution through ViaCEP
* External payment provider integration
* Authentication and authorization
* Paginated and filterable resource queries
* HATEOAS-based API navigation

---

## Technology Stack

* **Java 21**
* **Spring Boot 4.1**
* **Spring Web**
* **Spring Data JPA**
* **Spring Modulith**
* **Spring Security / OAuth2**
* **Spring HATEOAS**
* **Flyway**
* **H2 / PostgreSQL**
* **MapStruct**
* **Lombok**
* **JUnit 5**
* **Mockito**
* **AssertJ**
* **REST Assured**
* **JaCoCo**
* **Maven**

---

## Architecture

The application follows a **Modular Monolith** architecture using Spring Modulith.

Each module represents a business capability and contains its own domain model, persistence layer, application services, HTTP endpoints, projections, DTOs, and supporting infrastructure.

```text
Modular Monolith
├── people
│   ├── domain
│   ├── repository
│   ├── projection
│   ├── mapper
│   ├── service
│   ├── specification
│   ├── exception
│   └── controller
│       ├── dto
│       └── assembler
│
├── hotel
├── reservation
├── payment
├── account
├── authentication
└── shared
```

Module boundaries are validated through Spring Modulith to reduce unintended coupling between business contexts.

Cross-module communication is performed through explicitly exposed interfaces instead of direct access to internal repositories or services whenever appropriate.

---

## Important Architectural Decisions

* **Modular Monolith** as the primary architectural style
* **Rich Domain Model** for business rules and state transitions
* **Long IDs internally and UUIDs externally**
* **Flyway as the source of truth for database schema evolution**
* **DTOs as the external HTTP contract**
* **Interface projections for optimized read queries**
* **JPA entities used for state-changing operations**
* **Lazy loading by default for entity associations when appropriate**
* **HATEOAS for resource navigation and available actions**
* **Value Objects for domain-specific values such as Email and CPF**
* **Explicit module boundaries through Spring Modulith**
* **Database constraints as the final integrity layer**
* **Unit and integration tests separated by responsibility**

---

## Query Strategy

Read and write operations follow different strategies according to their purpose.

### Read Operations

Optimized projections are used when the API does not require the complete entity graph.

```text
Database
    ↓
Projection
    ↓
Service
    ↓
Mapper
    ↓
DTO
    ↓
REST Response
```

This approach reduces unnecessary column loading and avoids exposing persistence entities through the HTTP API.

### Write Operations

State-changing operations load the corresponding domain entity so business rules remain encapsulated inside the domain model.

```text
Database
    ↓
Entity
    ↓
Domain Method
    ↓
JPA Dirty Checking
    ↓
Database Update
```

Examples include:

* Activating an employee
* Placing an employee on leave
* Terminating an employment contract
* Changing a job position
* Updating hotel information
* Adding or removing hotel amenities

---

## API Design

The API follows resource-oriented REST conventions.

Current base endpoints include:

```text
/api/v1/people
/api/v1/employees
/api/v1/job-positions
/api/v1/hotels
```

List endpoints support pagination and, where applicable, optional search filters.

Example:

```http
GET /api/v1/employees?page=0&size=20
```

Individual resources are identified externally through UUIDs:

```http
GET /api/v1/employees/{uuid}
```

State transitions are exposed through explicit resource actions when appropriate:

```http
PATCH /api/v1/employees/{uuid}/job-position
PATCH /api/v1/employees/{uuid}/leave
PATCH /api/v1/employees/{uuid}/termination
```

HATEOAS links are included in selected responses to expose navigation and valid resource actions according to the current domain state.

---

## Database and Persistence

Database schema evolution is managed exclusively through Flyway migrations.

Hibernate is used primarily for entity mapping and schema validation rather than schema generation.

The persistence model uses:

* Numeric primary keys for internal relationships
* UUIDs for public resource identification
* Foreign key constraints
* Unique constraints
* Embedded Value Objects
* Enum persistence using string representations
* Collection tables for fixed enum collections such as hotel amenities

---

## How to Run

> Configuration and execution instructions will be added as the project infrastructure is finalized.

Planned execution flow:

```bash
mvn clean verify
mvn spring-boot:run
```

Environment-specific configuration will be provided through Spring profiles and environment variables.

---

## Testing and Quality

The project uses automated tests at multiple levels to validate domain behavior, application orchestration, persistence, and HTTP integration.

### Unit Tests

Unit tests focus on isolated domain and application behavior without requiring the complete Spring application context.

They primarily validate:

* Entity invariants
* Business rules
* Value Object validation
* Domain state transitions
* Valid and invalid scenarios
* Service orchestration using mocked dependencies
* Exception behavior

The main testing libraries used are:

* JUnit 5
* Mockito
* AssertJ

### Integration Tests

Integration tests validate the interaction between multiple application layers and infrastructure components.

They cover scenarios such as:

* REST endpoints
* Request and response serialization
* Bean Validation
* JPA persistence
* Flyway-managed schemas
* Projections and custom queries
* Pagination
* Global exception handling
* HTTP status codes
* Complete application flows

The application is started using a dedicated test profile, and REST endpoints are exercised using REST Assured.

### JaCoCo Coverage

JaCoCo is used to measure automated test coverage across both unit and integration tests.

Coverage reports are generated during the Maven `verify` lifecycle:

```bash
mvn clean verify
```

The HTML report is generated at:

```text
target/site/jacoco/index.html
```

Coverage is treated as a supporting quality metric rather than an isolated target. The primary focus remains on testing meaningful business rules, failure scenarios, state transitions, and integration behavior.

---

## Project Status / Roadmap

```text
✅ People
🚧 Hotel
⏳ ViaCEP / Address
⏳ Rooms
⏳ Reservations
⏳ Stay / Check-in / Check-out
⏳ Payments
⏳ Account
⏳ Authentication / Authorization
```

The project is being developed incrementally, with each module receiving domain modeling, persistence, API endpoints, validation, and automated tests before the next major capability is introduced.
