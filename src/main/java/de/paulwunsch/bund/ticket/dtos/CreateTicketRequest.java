package de.paulwunsch.bund.ticket.dtos;

import de.paulwunsch.bund.ticket.models.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTicketRequest(
        @NotBlank String subject,
        @Size(max = 4000) String description,
        @NotNull TicketPriority priority,
        @NotNull UUID requesterId,
        @NotNull UUID serviceOfferingId
) {}
