package com.sau.ticket_service.service;

import com.sau.ticket_service.dto.WorklogCreateRequest;
import com.sau.ticket_service.dto.WorklogResponseDTO;
import com.sau.ticket_service.exception.TicketNotFoundException;
import com.sau.ticket_service.exception.WorklogNotFoundException;
import com.sau.ticket_service.model.Ticket;
import com.sau.ticket_service.model.Worklog;
import com.sau.ticket_service.repository.TicketRepository;
import com.sau.ticket_service.repository.WorklogRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WorklogService {

  private static final Logger log = LoggerFactory.getLogger(
    WorklogService.class
  );

  private final TicketRepository ticketRepository;
  private final WorklogRepository worklogRepository;

  public WorklogService(
    TicketRepository ticketRepository,
    WorklogRepository worklogRepository
  ) {
    this.ticketRepository = ticketRepository;
    this.worklogRepository = worklogRepository;
  }

  public WorklogResponseDTO addWorklog(
    Long ticketId,
    WorklogCreateRequest req
  ) {
    Ticket ticket = ticketRepository
      .findById(ticketId)
      .orElseThrow(() -> new TicketNotFoundException(ticketId));

    Worklog w = new Worklog();
    w.setTicket(ticket);
    w.setAuthorId(req.getAuthorId());
    w.setAuthorUsername(req.getAuthorUsername());
    w.setWorkDate(req.getWorkDate());
    w.setSpentMinutes(req.getSpentMinutes());
    w.setDescription(req.getDescription());

    Worklog saved = worklogRepository.save(w);

    log.info(
      "Worklog added. ticketId={}, worklogId={}, spentMinutes={}",
      ticketId,
      saved.getId(),
      saved.getSpentMinutes()
    );
    return WorklogResponseDTO.from(saved);
  }

  @Transactional(readOnly = true)
  public List<WorklogResponseDTO> listWorklogs(Long ticketId) {
    // Ticket var mı? (isteğe bağlı) — raporlama için iyi
    if (!ticketRepository.existsById(ticketId)) {
      throw new TicketNotFoundException(ticketId);
    }

    return worklogRepository
      .findByTicketIdOrderByWorkDateDesc(ticketId)
      .stream()
      .map(WorklogResponseDTO::from)
      .toList();
  }

  public void deleteWorklog(Long worklogId) {
    Worklog w = worklogRepository
      .findById(worklogId)
      .orElseThrow(() -> new WorklogNotFoundException(worklogId));

    worklogRepository.delete(w);
    log.info("Worklog deleted. worklogId={}", worklogId);
  }
}
