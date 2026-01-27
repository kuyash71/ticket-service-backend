package com.sau.ticket_service.exception;

import com.sau.ticket_service.dto.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TicketNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNotFound(
    TicketNotFoundException ex
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "NOT_FOUND",
        ex.getMessage(),
        LocalDateTime.now()
      )
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
    MethodArgumentNotValidException ex
  ) {
    String details = ex
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .map(e -> e.getField() + ": " + e.getDefaultMessage())
      .collect(Collectors.joining(", "));

    return ResponseEntity.badRequest().body(
      new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "VALIDATION_ERROR",
        details,
        LocalDateTime.now()
      )
    );
  }

  // JSON parse / enum hataları gibi: priority=VERY_HIGH
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleNotReadable(
    HttpMessageNotReadableException ex
  ) {
    return ResponseEntity.badRequest().body(
      new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "INVALID_REQUEST",
        "Request body is invalid or contains wrong enum value.",
        LocalDateTime.now()
      )
    );
  }

  // RequestParam / PathVariable validasyonu için (ileride çok işe yarar)
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
    ConstraintViolationException ex
  ) {
    return ResponseEntity.badRequest().body(
      new ApiErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "VALIDATION_ERROR",
        ex.getMessage(),
        LocalDateTime.now()
      )
    );
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleNoHandler(
    NoHandlerFoundException ex
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "NOT_FOUND",
        "Endpoint not found",
        LocalDateTime.now()
      )
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      new ApiErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "INTERNAL_ERROR",
        ex.getMessage(),
        LocalDateTime.now()
      )
    );
  }

  @ExceptionHandler(InvalidTicketStatusTransitionException.class)
  public ResponseEntity<ApiErrorResponse> handleInvalidTransition(
    InvalidTicketStatusTransitionException ex
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
      new ApiErrorResponse(
        HttpStatus.CONFLICT.value(),
        "INVALID_STATUS_TRANSITION",
        ex.getMessage(),
        LocalDateTime.now()
      )
    );
  }

  @ExceptionHandler(WorklogNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleWorklogNotFound(
    WorklogNotFoundException ex
  ) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      new ApiErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "NOT_FOUND",
        ex.getMessage(),
        LocalDateTime.now()
      )
    );
  }
}
