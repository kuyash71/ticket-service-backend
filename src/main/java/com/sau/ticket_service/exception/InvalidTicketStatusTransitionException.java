package com.sau.ticket_service.exception;

import com.sau.ticket_service.model.TicketStatus;

public class InvalidTicketStatusTransitionException extends RuntimeException {

  public InvalidTicketStatusTransitionException(
    Long ticketId,
    TicketStatus from,
    TicketStatus to
  ) {
    super(
      "Invalid status transition for ticket " +
        ticketId +
        ": " +
        from +
        " -> " +
        to
    );
  }
}
