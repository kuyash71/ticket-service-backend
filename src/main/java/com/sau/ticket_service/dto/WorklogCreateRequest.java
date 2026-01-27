package com.sau.ticket_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorklogCreateRequest {

  @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  private String authorId;

  @Schema(example = "fatih")
  private String authorUsername;

  @Schema(example = "2026-01-27T12:00:00")
  @NotNull
  private LocalDateTime workDate;

  @Schema(example = "30")
  @NotNull
  @Min(1)
  private Integer spentMinutes;

  @Schema(example = "Investigated logs and reproduced issue.")
  @NotBlank
  private String description;
}
