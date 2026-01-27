package com.sau.ticket_service.controller;

import com.sau.ticket_service.dto.AssigneeUpdateRequest;
import com.sau.ticket_service.dto.TicketRequestDTO;
import com.sau.ticket_service.dto.TicketResponseDTO;
import com.sau.ticket_service.dto.TicketStatusUpdateRequest;
import com.sau.ticket_service.dto.WorklogCreateRequest;
import com.sau.ticket_service.dto.WorklogResponseDTO;
import com.sau.ticket_service.service.TicketService;
import com.sau.ticket_service.service.WorklogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
  name = "Tickets",
  description = "Ticket management endpoints (CRUD, status, assignee, worklogs)"
)
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

  private final TicketService ticketService;
  private final WorklogService worklogService;

  public TicketController(
    TicketService ticketService,
    WorklogService worklogService
  ) {
    this.ticketService = ticketService;
    this.worklogService = worklogService;
  }

  @Operation(summary = "Create a new ticket")
  @PostMapping
  public ResponseEntity<TicketResponseDTO> create(
    @Valid @RequestBody TicketRequestDTO dto
  ) {
    TicketResponseDTO created = ticketService.create(dto);
    return ResponseEntity.created(
      URI.create("/api/tickets/" + created.getId())
    ).body(created);
  }

  @Operation(summary = "Get ticket by id")
  @GetMapping("/{id}")
  public ResponseEntity<TicketResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(ticketService.getById(id));
  }

  @Operation(summary = "List all tickets")
  @GetMapping
  public ResponseEntity<List<TicketResponseDTO>> getAll() {
    return ResponseEntity.ok(ticketService.getAll());
  }

  @Operation(summary = "Update ticket status (with transition validation)")
  @PatchMapping("/{id}/status")
  public ResponseEntity<TicketResponseDTO> updateStatus(
    @PathVariable Long id,
    @Valid @RequestBody TicketStatusUpdateRequest request
  ) {
    return ResponseEntity.ok(
      ticketService.updateStatus(id, request.getStatus())
    );
  }

  @Operation(
    summary = "Assign ticket to an agent (assigneeId = Keycloak userId)"
  )
  @PatchMapping("/{id}/assignee")
  public ResponseEntity<TicketResponseDTO> updateAssignee(
    @PathVariable Long id,
    @Valid @RequestBody AssigneeUpdateRequest request
  ) {
    return ResponseEntity.ok(
      ticketService.updateAssignee(id, request.getAssigneeId())
    );
  }

  @Operation(summary = "Clear ticket assignee")
  @DeleteMapping("/{id}/assignee")
  public ResponseEntity<TicketResponseDTO> clearAssignee(
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(ticketService.clearAssignee(id));
  }

  @Operation(summary = "Add worklog entry to a ticket")
  @PostMapping("/{id}/worklogs")
  public ResponseEntity<WorklogResponseDTO> addWorklog(
    @PathVariable Long id,
    @Valid @RequestBody WorklogCreateRequest request
  ) {
    return ResponseEntity.ok(worklogService.addWorklog(id, request));
  }

  @Operation(summary = "List worklogs for a ticket")
  @GetMapping("/{id}/worklogs")
  public ResponseEntity<List<WorklogResponseDTO>> listWorklogs(
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(worklogService.listWorklogs(id));
  }

  @DeleteMapping("/worklogs/{worklogId}")
  public ResponseEntity<Void> deleteWorklog(@PathVariable Long worklogId) {
    worklogService.deleteWorklog(worklogId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    ticketService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
