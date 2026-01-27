# IT Ticket Management – Backend Architecture

## Overview

Backend is a Spring Boot REST API running in Docker. It provides ticket lifecycle management, assignment and worklog tracking.
The system is designed with a layered architecture and DTO-based API contracts.

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- Docker Compose
- OpenAPI/Swagger (dev profile)

## Layered Architecture

### Controller Layer

- Exposes REST endpoints under `/api/...`
- Validates request bodies using `jakarta.validation`
- Returns DTOs (`TicketResponseDTO`, `WorklogResponseDTO`) instead of exposing entities

### Service Layer

- Contains business rules and workflow logic
- Enforces ticket status transition rules (workflow)
- Logs important business events (create, status change, assignment, worklog)

### Repository Layer

- Spring Data JPA repositories for persistence
- `TicketRepository`, `WorklogRepository`

## DTO Strategy

- Request DTOs: used for validation and API contract (`TicketRequestDTO`, `TicketStatusUpdateRequest`, etc.)
- Response DTOs: read-only representation returned to clients (`TicketResponseDTO`, `WorklogResponseDTO`)
  This prevents exposing internal entity structure and keeps API stable.

## Error Handling

Global exception handling is implemented via `@RestControllerAdvice`:

- `TicketNotFoundException` → 404 NOT_FOUND
- Validation errors (`MethodArgumentNotValidException`) → 400 VALIDATION_ERROR
- Invalid enum / JSON body → 400 INVALID_REQUEST
- Invalid status transition → 409 INVALID_STATUS_TRANSITION
- Other errors → 500 INTERNAL_ERROR

All error responses use a standard `ApiErrorResponse` format.

## Swagger / OpenAPI

Swagger UI is enabled only in the `dev` profile:

- `/swagger-ui/index.html`
- `/v3/api-docs`

Production profile can disable Swagger endpoints.

## Docker Compose

Containers:

- PostgreSQL (ticket database)
- Backend (Spring Boot API)
- Frontend (React)
- Keycloak (authentication – integration planned)
