package de.paulwunsch.bund.ticket.dtos;

import de.paulwunsch.bund.ticket.models.TicketPriority;
import de.paulwunsch.bund.ticket.models.TicketStatus;

import java.util.UUID;

public record TicketResponse(
        UUID id,
        String subject,
        String description,
        TicketStatus status,
        TicketPriority priority,
        UUID requesterId,
        UUID assigneeId,
        UUID serviceOfferingId,
        String createdAt,
        String updatedAt
) {}
