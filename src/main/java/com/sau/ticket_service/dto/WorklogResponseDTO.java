package com.sau.ticket_service.dto;

import com.sau.ticket_service.model.Worklog;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorklogResponseDTO {

  private Long id;
  private Long ticketId;
  private String authorId;
  private String authorUsername;
  private Integer spentMinutes;
  private LocalDateTime workDate;
  private String description;
  private LocalDateTime createdAt;

  public static WorklogResponseDTO from(Worklog w) {
    return new WorklogResponseDTO(
      w.getId(),
      w.getTicket().getId(),
      w.getAuthorId(),
      w.getAuthorUsername(),
      w.getSpentMinutes(),
      w.getWorkDate(),
      w.getDescription(),
      w.getCreatedAt()
    );
  }
}
