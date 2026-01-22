package com.sau.ticket_service.controller;

import com.sau.ticket_service.dto.TicketRequestDTO;
import com.sau.ticket_service.dto.TicketResponseDTO;
import com.sau.ticket_service.model.TicketStatus;
import com.sau.ticket_service.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sau.ticket_service.dto.TicketStatusUpdateRequest;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(@Valid @RequestBody TicketRequestDTO dto) {
        TicketResponseDTO created = ticketService.create(dto);
        return ResponseEntity
                .created(URI.create("/api/tickets/" + created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAll() {
        return ResponseEntity.ok(ticketService.getAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDTO> updateStatus(
        @PathVariable Long id,
        @Valid @RequestBody TicketStatusUpdateRequest request
    ) {
    return ResponseEntity.ok(ticketService.updateStatus(id, request.getStatus()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
