package com.sau.ticket_service.dto;

import com.sau.ticket_service.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @Size(max = 2000, message = "Description can be max 2000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}
