package de.paulwunsch.bund.ticket.services;

import de.paulwunsch.bund.ticket.dtos.CreateTicketRequest;
import de.paulwunsch.bund.ticket.dtos.TicketResponse;
import de.paulwunsch.bund.ticket.dtos.UpdateTicketStatusRequest;
import de.paulwunsch.bund.ticket.models.ServiceOffering;
import de.paulwunsch.bund.ticket.models.Ticket;
import de.paulwunsch.bund.ticket.models.TicketStatus;
import de.paulwunsch.bund.ticket.models.User;
import de.paulwunsch.bund.ticket.repositories.ServiceOfferingRepository;
import de.paulwunsch.bund.ticket.repositories.TicketRepository;
import de.paulwunsch.bund.ticket.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;
    private final ServiceOfferingRepository svcRepo;

    public TicketService(TicketRepository ticketRepo, UserRepository userRepo, ServiceOfferingRepository svcRepo) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
        this.svcRepo = svcRepo;
    }

    public TicketResponse create(CreateTicketRequest req) {

        log.info("Create ticket: subject='{}', requesterId={}, serviceOfferingId={}",
                req.subject(), req.requesterId(), req.serviceOfferingId());

        User requester = userRepo.findById(req.requesterId())
                .orElseThrow(() -> new EntityNotFoundException("Requester not found"));
        ServiceOffering svc = svcRepo.findById(req.serviceOfferingId())
                .orElseThrow(() -> new EntityNotFoundException("Service offering not found"));

        Ticket t = new Ticket();
        t.setSubject(req.subject());
        t.setDescription(req.description());
        t.setPriority(req.priority());
        t.setStatus(TicketStatus.OPEN);
        t.setRequester(requester);
        t.setServiceOffering(svc);

        Ticket saved = ticketRepo.save(t);

        log.info("Ticket created: id={}, status={}", saved.getId(), saved.getStatus());

        return toResponse(saved);
    }

    public TicketResponse get(UUID id) {
        log.debug("Get ticket: id={}", id);
        return ticketRepo.findById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }

    public TicketResponse updateStatus(UUID id, UpdateTicketStatusRequest req, UserRepository userRepo) {
        log.info("Update ticket status: id={}, newStatus={}, assigneeId={}",
                id, req.status(), req.assigneeId());

        Ticket t = ticketRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
        t.setStatus(req.status());
        if (req.assigneeId() != null) {
            User assignee = userRepo.findById(req.assigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found"));
            t.setAssignee(assignee);
        }

        log.info("Ticket updated: id={}, status={}, assigneeId={}",
                t.getId(), t.getStatus(), t.getAssignee() != null ? t.getAssignee().getId() : null);

        return toResponse(t);
    }

    private TicketResponse toResponse(Ticket t) {
        return new TicketResponse(
                t.getId(),
                t.getSubject(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getRequester() != null ? t.getRequester().getId() : null,
                t.getAssignee() != null ? t.getAssignee().getId() : null,
                t.getServiceOffering() != null ? t.getServiceOffering().getId() : null,
                t.getCreatedAt() != null ? t.getCreatedAt().toString() : null,
                t.getUpdatedAt() != null ? t.getUpdatedAt().toString() : null
        );
    }
}
