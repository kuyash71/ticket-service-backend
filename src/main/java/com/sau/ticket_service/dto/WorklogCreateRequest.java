package com.sau.ticket_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorklogCreateRequest {

  // Şimdilik opsiyonel; Keycloak gelince token’dan dolduracağız
  private String authorId;
  private String authorUsername;

  @NotNull
  private LocalDateTime workDate;

  @NotNull
  @Min(1)
  private Integer spentMinutes;

  @NotBlank
  private String description;
}
