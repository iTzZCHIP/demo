package de.paulwunsch.bund.ticket.controller;

import de.paulwunsch.bund.ticket.dtos.CreateTicketRequest;
import de.paulwunsch.bund.ticket.dtos.TicketResponse;
import de.paulwunsch.bund.ticket.dtos.UpdateTicketStatusRequest;
import de.paulwunsch.bund.ticket.repositories.TicketRepository;
import de.paulwunsch.bund.ticket.repositories.UserRepository;
import de.paulwunsch.bund.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@Validated
public class TicketController {

    private final TicketService ticketService;
    private final TicketRepository ticketRepo;
    private final UserRepository userRepo;

    public TicketController(TicketService ticketService, TicketRepository ticketRepo, UserRepository userRepo) {
        this.ticketService = ticketService;
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public TicketResponse create(@Valid @RequestBody CreateTicketRequest req) {
        return ticketService.create(req);
    }

    @GetMapping("/{id}")
    public TicketResponse get(@PathVariable UUID id) {
        return ticketService.get(id);
    }

    @GetMapping
    public Page<TicketResponse> list(Pageable pageable) {
        return ticketRepo.findAll(pageable).map(t ->
                new TicketResponse(
                        t.getId(), t.getSubject(), t.getDescription(), t.getStatus(), t.getPriority(),
                        t.getRequester() != null ? t.getRequester().getId() : null,
                        t.getAssignee() != null ? t.getAssignee().getId() : null,
                        t.getServiceOffering() != null ? t.getServiceOffering().getId() : null,
                        t.getCreatedAt() != null ? t.getCreatedAt().toString() : null,
                        t.getUpdatedAt() != null ? t.getUpdatedAt().toString() : null
                )
        );
    }

    @PatchMapping("/{id}/status")
    public TicketResponse updateStatus(@PathVariable UUID id, @Valid @RequestBody UpdateTicketStatusRequest req) {
        return ticketService.updateStatus(id, req, userRepo);
    }
}
