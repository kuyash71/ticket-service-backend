package com.sau.ticket_service.exception;

public class WorklogNotFoundException extends RuntimeException {

  public WorklogNotFoundException(Long id) {
    super("Worklog not found: " + id);
  }
}
