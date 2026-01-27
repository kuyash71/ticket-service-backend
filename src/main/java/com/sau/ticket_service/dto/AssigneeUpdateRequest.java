package com.sau.ticket_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssigneeUpdateRequest {

  @NotBlank
  private String assigneeId;
}
