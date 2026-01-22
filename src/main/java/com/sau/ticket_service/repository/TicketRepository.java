package com.sau.ticket_service.repository;

import com.sau.ticket_service.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
