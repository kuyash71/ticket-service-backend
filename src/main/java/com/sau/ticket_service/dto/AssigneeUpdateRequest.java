package com.sau.ticket_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssigneeUpdateRequest {

  @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  @NotBlank
  private String assigneeId;
}
