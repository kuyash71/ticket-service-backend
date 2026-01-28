# API Overview (Ticket Service)

This document describes the REST API endpoints of the Ticket Service, including request/response examples and common error formats.

## Base URL

All endpoints are served under:

- `http://localhost:8080/api`

Swagger (dev profile):

- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

---

## Standard Error Response Format

All errors return the same JSON structure (`ApiErrorResponse`):

```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Ticket not found: 10",
  "timestamp": "2026-01-27T12:34:56"
}
```

Common error codes:

- `400 VALIDATION_ERROR` – Bean validation errors (missing/invalid fields)
- `400 INVALID_REQUEST` – Invalid JSON body or invalid enum value
- `404 NOT_FOUND` – Ticket/Worklog not found
- `409 INVALID_STATUS_TRANSITION` – Invalid workflow transition (status update not allowed)
- `500 INTERNAL_ERROR` – Unexpected server error

---

# Tickets

## 1) Create Ticket

Create a new ticket (default status: `OPEN`).

- **POST** `/tickets`

Request body:

```json
{
  "title": "Cannot login to VPN",
  "description": "After password reset, VPN connection fails with error 720.",
  "priority": "HIGH"
}
```

Successful response: **201 Created**

- `Location: /api/tickets/{id}`

Response body (`TicketResponseDTO`):

```json
{
  "id": 1,
  "title": "Cannot login to VPN",
  "description": "After password reset, VPN connection fails with error 720.",
  "status": "OPEN",
  "priority": "HIGH",
  "assigneeId": null,
  "createdAt": "2026-01-27T12:00:00",
  "updatedAt": "2026-01-27T12:00:00"
}
```

Possible errors:

- **400** `VALIDATION_ERROR` (missing title/description/priority etc.)
- **400** `INVALID_REQUEST` (invalid enum, malformed JSON)

---

## 2) List Tickets

List all tickets.

- **GET** `/tickets`

Response: **200 OK**

```json
[
  {
    "id": 1,
    "title": "Cannot login to VPN",
    "description": "After password reset, VPN connection fails with error 720.",
    "status": "OPEN",
    "priority": "HIGH",
    "assigneeId": null,
    "createdAt": "2026-01-27T12:00:00",
    "updatedAt": "2026-01-27T12:00:00"
  }
]
```

---

## 3) Get Ticket by Id

Fetch a ticket by its id.

- **GET** `/tickets/{id}`

Response: **200 OK** (same as TicketResponseDTO)

Possible errors:

- **404** `NOT_FOUND`

---

## 4) Update Ticket Status (Workflow)

Update ticket status while enforcing valid transitions.

- **PATCH** `/tickets/{id}/status`

Request body:

```json
{ "status": "IN_PROGRESS" }
```

Response: **200 OK** (TicketResponseDTO)

### Allowed transitions (current implementation)

- `OPEN` → `IN_PROGRESS` or `CLOSED`
- `IN_PROGRESS` → `RESOLVED` or `OPEN` or `WAITING_CUSTOMER`
- `WAITING_CUSTOMER` → `IN_PROGRESS` or `RESOLVED`
- `RESOLVED` → `CLOSED` or `IN_PROGRESS`
- `CLOSED` → (no transitions allowed)

Possible errors:

- **400** `VALIDATION_ERROR` (status missing)
- **400** `INVALID_REQUEST` (invalid enum value)
- **404** `NOT_FOUND` (ticket does not exist)
- **409** `INVALID_STATUS_TRANSITION` (workflow rule violated)

Example invalid transition response (**409**):

```json
{
  "status": 409,
  "error": "INVALID_STATUS_TRANSITION",
  "message": "Invalid status transition for ticket 1: OPEN -> RESOLVED",
  "timestamp": "2026-01-27T12:10:00"
}
```

---

## 5) Assign Ticket (Assignee)

Assign a ticket to an agent. `assigneeId` is designed to store Keycloak user id (subject) in the future.

- **PATCH** `/tickets/{id}/assignee`

Request body:

```json
{ "assigneeId": "3fa85f64-5717-4562-b3fc-2c963f66afa6" }
```

Response: **200 OK** (TicketResponseDTO)

Possible errors:

- **400** `VALIDATION_ERROR` (assigneeId missing/blank)
- **404** `NOT_FOUND`

---

## 6) Clear Assignee

Remove assignee from a ticket.

- **DELETE** `/tickets/{id}/assignee`

Response: **200 OK** (TicketResponseDTO with `assigneeId: null`)

Possible errors:

- **404** `NOT_FOUND`

---

## 7) Delete Ticket

Delete a ticket by its id.

- **DELETE** `/tickets/{id}`

Response: **204 No Content**

Possible errors:

- **404** `NOT_FOUND`

---

# Worklogs

Worklogs represent time tracking entries for tickets.

## 1) Add Worklog to Ticket

- **POST** `/tickets/{id}/worklogs`

Request body:

```json
{
  "authorId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "authorUsername": "agent1",
  "workDate": "2026-01-27T12:00:00",
  "spentMinutes": 30,
  "description": "Investigated logs and reproduced the issue."
}
```

Response: **200 OK**

```json
{
  "id": 10,
  "ticketId": 1,
  "authorId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "authorUsername": "agent1",
  "spentMinutes": 30,
  "workDate": "2026-01-27T12:00:00",
  "description": "Investigated logs and reproduced the issue.",
  "createdAt": "2026-01-27T12:05:00"
}
```

Possible errors:

- **400** `VALIDATION_ERROR` (missing workDate/spentMinutes/description, spentMinutes < 1)
- **404** `NOT_FOUND` (ticket does not exist)

---

## 2) List Worklogs for Ticket

- **GET** `/tickets/{id}/worklogs`

Response: **200 OK**

```json
[
  {
    "id": 10,
    "ticketId": 1,
    "authorId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "authorUsername": "agent1",
    "spentMinutes": 30,
    "workDate": "2026-01-27T12:00:00",
    "description": "Investigated logs and reproduced the issue.",
    "createdAt": "2026-01-27T12:05:00"
  }
]
```

Possible errors:

- **404** `NOT_FOUND` (ticket does not exist)

---

## 3) Delete Worklog

- **DELETE** `/tickets/worklogs/{worklogId}`

Response: **204 No Content**

Possible errors:

- **404** `NOT_FOUND` (worklog does not exist)
