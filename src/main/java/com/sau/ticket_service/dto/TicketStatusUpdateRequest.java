package com.sau.ticket_service.dto;

import com.sau.ticket_service.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private TicketStatus status;
}
