package de.paulwunsch.bund.ticket.repositories;

import de.paulwunsch.bund.ticket.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository <Ticket, UUID> {}
