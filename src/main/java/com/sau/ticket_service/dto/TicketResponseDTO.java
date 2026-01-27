package com.sau.ticket_service.dto;

import com.sau.ticket_service.model.Ticket;
import com.sau.ticket_service.model.TicketPriority;
import com.sau.ticket_service.model.TicketStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TicketResponseDTO {

  private Long id;
  private String title;
  private String description;
  private TicketStatus status;
  private TicketPriority priority;

  private String assigneeId;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TicketResponseDTO from(Ticket t) {
    return new TicketResponseDTO(
      t.getId(),
      t.getTitle(),
      t.getDescription(),
      t.getStatus(),
      t.getPriority(),
      t.getAssigneeId(),
      t.getCreatedAt(),
      t.getUpdatedAt()
    );
  }
}
