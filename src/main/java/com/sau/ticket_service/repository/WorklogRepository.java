package com.sau.ticket_service.repository;

import com.sau.ticket_service.model.Worklog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorklogRepository extends JpaRepository<Worklog, Long> {
  List<Worklog> findByTicketIdOrderByWorkDateDesc(Long ticketId);
}
