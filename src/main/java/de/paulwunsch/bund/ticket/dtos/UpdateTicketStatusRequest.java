package de.paulwunsch.bund.ticket.dtos;

import de.paulwunsch.bund.ticket.models.TicketStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateTicketStatusRequest(
        @NotNull TicketStatus status,
        UUID assigneeId
) {}
