package com.sau.ticket_service.service;

import com.sau.ticket_service.dto.TicketRequestDTO;
import com.sau.ticket_service.dto.TicketResponseDTO;
import com.sau.ticket_service.exception.InvalidTicketStatusTransitionException;
import com.sau.ticket_service.exception.TicketNotFoundException;
import com.sau.ticket_service.model.Ticket;
import com.sau.ticket_service.model.TicketStatus;
import com.sau.ticket_service.repository.TicketRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

  private static final Logger log = LoggerFactory.getLogger(
    TicketService.class
  );

  private final TicketRepository ticketRepository;

  public TicketService(TicketRepository ticketRepository) {
    this.ticketRepository = ticketRepository;
  }

  public TicketResponseDTO create(TicketRequestDTO dto) {
    Ticket ticket = new Ticket();
    ticket.setTitle(dto.getTitle());
    ticket.setDescription(dto.getDescription());
    ticket.setPriority(dto.getPriority());

    // default status
    ticket.setStatus(TicketStatus.OPEN);

    Ticket saved = ticketRepository.save(ticket);
    log.info(
      "Ticket created. ticketId={}, status={}",
      saved.getId(),
      saved.getStatus()
    );
    return TicketResponseDTO.from(saved);
  }

  @Transactional(readOnly = true)
  public TicketResponseDTO getById(Long id) {
    Ticket t = ticketRepository
      .findById(id)
      .orElseThrow(() -> new TicketNotFoundException(id));
    return TicketResponseDTO.from(t);
  }

  @Transactional(readOnly = true)
  public List<TicketResponseDTO> getAll() {
    return ticketRepository
      .findAll()
      .stream()
      .map(TicketResponseDTO::from)
      .toList();
  }

  public TicketResponseDTO updateStatus(Long id, TicketStatus newStatus) {
    Ticket ticket = ticketRepository
      .findById(id)
      .orElseThrow(() -> new TicketNotFoundException(id));

    TicketStatus oldStatus = ticket.getStatus();

    if (!isTransitionAllowed(oldStatus, newStatus)) {
      log.warn(
        "Invalid status transition. ticketId={}, {} -> {}",
        id,
        oldStatus,
        newStatus
      );
      throw new InvalidTicketStatusTransitionException(
        id,
        oldStatus,
        newStatus
      );
    }

    ticket.setStatus(newStatus);
    Ticket saved = ticketRepository.save(ticket);

    log.info(
      "Ticket status changed. ticketId={}, {} -> {}",
      id,
      oldStatus,
      newStatus
    );
    return TicketResponseDTO.from(saved);
  }

  private boolean isTransitionAllowed(TicketStatus from, TicketStatus to) {
    if (from == to) return true;

    return switch (from) {
      case OPEN -> (to == TicketStatus.IN_PROGRESS ||
        to == TicketStatus.CLOSED);
      case IN_PROGRESS -> (to == TicketStatus.WAITING_CUSTOMER ||
        to == TicketStatus.RESOLVED ||
        to == TicketStatus.OPEN);
      case WAITING_CUSTOMER -> (to == TicketStatus.IN_PROGRESS ||
        to == TicketStatus.RESOLVED);
      case RESOLVED -> (to == TicketStatus.CLOSED ||
        to == TicketStatus.IN_PROGRESS);
      case CLOSED -> false;
    };
  }

  public void delete(Long id) {
    if (!ticketRepository.existsById(id)) {
      throw new TicketNotFoundException(id);
    }
    ticketRepository.deleteById(id);
    log.info("Ticket deleted. ticketId={}", id);
  }

  public TicketResponseDTO updateAssignee(Long id, String assigneeId) {
    Ticket ticket = ticketRepository
      .findById(id)
      .orElseThrow(() -> new TicketNotFoundException(id));

    String old = ticket.getAssigneeId();
    ticket.setAssigneeId(assigneeId);

    Ticket saved = ticketRepository.save(ticket);
    log.info(
      "Ticket assignee changed. ticketId={}, {} -> {}",
      id,
      old,
      assigneeId
    );

    return TicketResponseDTO.from(saved);
  }

  public TicketResponseDTO clearAssignee(Long id) {
    Ticket ticket = ticketRepository
      .findById(id)
      .orElseThrow(() -> new TicketNotFoundException(id));

    String old = ticket.getAssigneeId();
    ticket.setAssigneeId(null);

    Ticket saved = ticketRepository.save(ticket);
    log.info("Ticket assignee cleared. ticketId={}, oldAssignee={}", id, old);

    return TicketResponseDTO.from(saved);
  }
}
