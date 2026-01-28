# IT Ticket Management – Analysis (First Milestone)

**Project:** IT Ticket Management (Customer Portal + Agent Operations)  
**Milestone:** 1st Meeting (January 28) – Analysis & Mock Preparation  
**Language:** EN (UI will support TR/EN)

---

## 1. Purpose & Vision

The purpose of this project is to develop an **IT Ticket Management** system where customers can create and track support requests (tickets), support agents can manage and resolve those tickets, and managers can monitor operational performance through reporting and SLA visibility.

The system is designed according to enterprise-grade standards:

- REST API–based architecture
- Containerized deployment using Docker
- Persistent data storage with PostgreSQL
- Prepared for future role-based security using Keycloak

The primary goal of the first milestone is to establish a **stable backend foundation**, define system behavior clearly, and prepare mock screens based on well-defined user flows.

---

## 2. Scope (In-Scope / Out-of-Scope)

### 2.1 In-Scope (MVP)

#### Customer

- Create tickets (title, description, priority)
- List own tickets and view ticket details

#### Agent

- Update ticket status according to workflow rules
- Assign and clear ticket assignees
- Add and list worklogs for tickets

#### System / Platform

- Standardized error response format (`ApiErrorResponse`)
- Swagger / OpenAPI documentation (dev profile)
- Docker Compose–based runtime environment

> MVP API scope and endpoint definitions are documented in `api-overview.md`.  
> Backend layered architecture and error handling strategy are documented in `architecture.md`.

---

### 2.2 Out-of-Scope (Planned After Meeting-1)

The following features are intentionally deferred to later sprints:

- Keycloak integration (authentication and role-based authorization)
- Enforcement of the rule “customer can view only own tickets”
- Attachment upload and content validation
- SLA calculation and SLA breach/risk views
- Notification system
- Logging pipeline to OpenSearch (optional Kafka pipeline)
- OpenTelemetry tracing and metrics
- jBPM workflow engine integration (bonus feature)

Authorization rules and role models are **defined at analysis level**, but **not yet enforced at runtime**. This is a deliberate decision to keep early iterations focused on core business functionality and API stability.

---

## 3. Roles & Authorization Model (Target)

The system is designed around three primary roles.

### 3.1 CUSTOMER

- Can create tickets
- Can list and view details of own tickets
- (Planned) Can add external comments to tickets

### 3.2 AGENT

- Can view ticket queues
- Can assign tickets to self
- Can update ticket status according to workflow rules
- Can add worklog entries

### 3.3 MANAGER

- Can access reporting dashboards
- (Planned) Can monitor SLA compliance and breach rates
- (Planned) Can view agent performance metrics

**Note:** Fields such as `assigneeId` and `authorId` are designed to store Keycloak user identifiers (subject IDs) once authentication is integrated.

---

## 4. Core Domain Objects (Initial)

The following domain objects form the basis of the MVP and future iterations.

### 4.1 Ticket (MVP)

- id
- title
- description
- priority (LOW / MEDIUM / HIGH)
- status (OPEN / IN_PROGRESS / RESOLVED / CLOSED / WAITING_CUSTOMER)
- assigneeId (nullable; future: Keycloak subject)
- createdAt / updatedAt

### 4.2 Worklog (MVP)

- id
- ticketId
- authorId (future: Keycloak subject)
- authorUsername (for display purposes)
- workDate
- spentMinutes
- description
- createdAt

### 4.3 Planned Entities (Post-MVP)

- Comment (internal / external)
- Attachment (metadata + storage reference)
- SLA definition / policy (priority-based)
- Notification (event store or user notification list)
- Audit log / event log (to be forwarded to OpenSearch)

---

## 5. Ticket Lifecycle & Workflow Rules

### 5.1 Current Implementation Workflow

In the current backend implementation, ticket status transitions are restricted as follows:

- `OPEN` → `IN_PROGRESS` or `CLOSED`
- `IN_PROGRESS` → `RESOLVED` or `OPEN` or `WAITING_CUSTOMER`
- `WAITING_CUSTOMER` → `IN_PROGRESS` or `RESOLVED`
- `RESOLVED` → `CLOSED` or `IN_PROGRESS`
- `CLOSED` → (no transitions allowed)

The purpose of enforcing workflow rules is:

- To prevent invalid or random status changes
- To allow the frontend to display only valid actions for a given ticket state

---

### 5.2 Specification Alignment (Status Mapping)

The status names defined in the specification (e.g., _New_, _Waiting for Customer_) do not have to match the current implementation one-to-one.

| Specification Status | Current Implementation |
| -------------------- | ---------------------- |
| New                  | OPEN                   |
| In Progress          | IN_PROGRESS            |
| Waiting for Customer | WAITING_CUSTOMER       |
| Resolved             | RESOLVED               |
| Closed               | CLOSED                 |

After Meeting-1, this mapping will be finalized based on mock screens and user flow decisions. The status enum may be extended or mapped at UI level if required.

---

## 6. API Contract Summary (MVP)

The full API contract, request/response examples, and error formats are documented in `api-overview.md`.

### Tickets

- POST `/api/tickets` (create)
- GET `/api/tickets` (list)
- GET `/api/tickets/{id}` (detail)
- PATCH `/api/tickets/{id}/status` (workflow enforced)
- PATCH `/api/tickets/{id}/assignee` (assign)
- DELETE `/api/tickets/{id}/assignee` (clear)
- DELETE `/api/tickets/{id}` (delete)

### Worklogs

- POST `/api/tickets/{id}/worklogs` (add)
- GET `/api/tickets/{id}/worklogs` (list)
- DELETE `/api/tickets/worklogs/{worklogId}` (delete)

---

## 7. Non-Functional Requirements (Planned)

The following non-functional requirements will be addressed in later sprints.

### 7.1 Security (Keycloak)

- OIDC-based login (frontend)
- JWT validation in backend (resource server)
- Role-based endpoint authorization (CUSTOMER / AGENT / MANAGER)

### 7.2 Logging & Observability

- Structured logging using log4j2
- Log forwarding to OpenSearch (optional Kafka pipeline)
- OpenTelemetry metrics and traces (latency, error rate, request count)

These components are intentionally deferred to keep the initial milestone focused on functional correctness and API stability.

---

## 8. Decisions & Assumptions (ADR Style)

### D1 – DTO-Based API Contract

- Request DTOs are used for validation
- Response DTOs prevent entity exposure and keep the API stable  
  (See `architecture.md` for details)

### D2 – Standard Error Response

All errors follow a single response format:

- status, error, message, timestamp  
  (See `api-overview.md` for details)

### D3 – Workflow-Enforced Status Updates

Ticket status updates are restricted by predefined transition rules.

### A1 – Identity Mapping

User-related fields (`assigneeId`, `authorId`) are currently stored as strings and will later be populated with Keycloak subject identifiers.

---

## 9. User Flows (Input for Mock Screens)

The following user flows are the **direct input** for the mock screen designs that will be presented in the first meeting.

### 9.1 Customer Flow

1. Login
2. Create Ticket
3. My Tickets (list + filter)
4. Ticket Detail (status, comments, attachments – planned)

### 9.2 Agent Flow

1. Login
2. Ticket Queue (unassigned / assigned to me)
3. Ticket Detail
   - Assign / Clear assignee
   - Status update (allowed transitions)
   - Worklog add/list
   - Internal notes/comments (planned)

### 9.3 Manager Flow (Planned)

1. Login
2. Dashboard / Reports
3. SLA view (risk / breach)
4. Agent performance metrics

---

## 10. Roadmap (January → June 7)

Dates are defined in sprint form and may be adjusted according to meeting feedback.

### Sprint 0 (Now → Meeting-1)

- Backend skeleton, API contract, architecture documentation ✅
- Analysis document ✅
- Lo-fi Mock screen wireframes ✅

### Sprint 1

- Keycloak integration (login + role model)
- Security Scenarios
- Customer “own tickets only” rule
- Login/Logout tests
- Role enforcement

### Sprint 2

- Comments (internal / external)
- Attachments (upload + storage + basic validation)
- Hi-fi mock screen wireframes
- Finale UI/UX designs
- Differentation of Public and Internal Worklog
- Basic layout and i18n via React
- Ticket list/detail pages

### Sprint 3

- SLA definition and SLA risk/breach calculation
- Reporting (status distribution, SLA compliance)
- Manager Dashboard (read-only)
- Dashboard via React + UI Polish
- Basic Logging & Monitoring

### Sprint 4

- Logging pipeline → OpenSearch (optional Kafka)
- OpenTelemetry metrics and traces
- Dashboards
- Full UI Integration
- Validation hardening
- E2E Demo
- Deploy/Docker polish

### Sprint 5 (Polish & Demo)

- UX improvements (TR/EN i18n)
- Validation hardening and edge cases
- Demo scenario and final documentation

---

## 11. Acceptance Criteria (Meeting-1)

The first milestone is considered complete when:

- Backend services are running via Docker Compose
- Ticket and Worklog APIs are accessible via Swagger
- DTO-based validation and standard error handling are implemented
- Analysis document and mock screen wireframes are prepared

---

## 12. References

- API Overview (Ticket Service) – endpoint contracts and examples
- Backend Architecture – layered design, DTO strategy, error handling
