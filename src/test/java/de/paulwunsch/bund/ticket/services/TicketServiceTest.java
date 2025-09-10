package de.paulwunsch.bund.ticket.services;

import de.paulwunsch.bund.ticket.dtos.CreateTicketRequest;
import de.paulwunsch.bund.ticket.dtos.TicketResponse;
import de.paulwunsch.bund.ticket.models.*;
import de.paulwunsch.bund.ticket.repositories.ServiceOfferingRepository;
import de.paulwunsch.bund.ticket.repositories.TicketRepository;
import de.paulwunsch.bund.ticket.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ServiceOfferingRepository svcRepo;

    @InjectMocks
    private TicketService ticketService;

    @Test
    void create_shouldCreateTicket_whenValidRequest() {
        UUID requesterId = UUID.randomUUID();
        UUID serviceOfferingId = UUID.randomUUID();
        CreateTicketRequest request = new CreateTicketRequest(
                "Test Subject",
                "Test Description",
                TicketPriority.HIGH,
                requesterId,
                serviceOfferingId
        );

        User requester = new User();
        requester.setId(requesterId);
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setId(serviceOfferingId);
        Ticket savedTicket = new Ticket();
        savedTicket.setId(UUID.randomUUID());
        savedTicket.setSubject(request.subject());
        savedTicket.setDescription(request.description());
        savedTicket.setPriority(request.priority());
        savedTicket.setStatus(TicketStatus.OPEN);
        savedTicket.setRequester(requester);
        savedTicket.setServiceOffering(serviceOffering);

        when(userRepo.findById(requesterId)).thenReturn(Optional.of(requester));
        when(svcRepo.findById(serviceOfferingId)).thenReturn(Optional.of(serviceOffering));
        when(ticketRepo.save(any(Ticket.class))).thenReturn(savedTicket);

        TicketResponse response = ticketService.create(request);

        assertNotNull(response);
        assertEquals(savedTicket.getId(), response.id());
        assertEquals(request.subject(), response.subject());
        assertEquals(request.description(), response.description());
        assertEquals(TicketStatus.OPEN, response.status());
        assertEquals(request.priority(), response.priority());
        assertEquals(requesterId, response.requesterId());
        assertEquals(serviceOfferingId, response.serviceOfferingId());

        verify(userRepo).findById(requesterId);
        verify(svcRepo).findById(serviceOfferingId);
        verify(ticketRepo).save(any(Ticket.class));
    }

    @Test
    void create_shouldThrowException_whenRequesterNotFound() {
        UUID requesterId = UUID.randomUUID();
        UUID serviceOfferingId = UUID.randomUUID();
        CreateTicketRequest request = new CreateTicketRequest(
                "Test Subject",
                "Test Description",
                TicketPriority.MEDIUM,
                requesterId,
                serviceOfferingId
        );

        when(userRepo.findById(requesterId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> ticketService.create(request));

        assertEquals("Requester not found", exception.getMessage());
        verify(userRepo).findById(requesterId);
        verifyNoInteractions(svcRepo);
        verifyNoInteractions(ticketRepo);
    }

    @Test
    void create_shouldThrowException_whenServiceOfferingNotFound() {
        UUID requesterId = UUID.randomUUID();
        UUID serviceOfferingId = UUID.randomUUID();
        CreateTicketRequest request = new CreateTicketRequest(
                "Test Subject",
                "Test Description",
                TicketPriority.LOW,
                requesterId,
                serviceOfferingId
        );

        User requester = new User();
        requester.setId(requesterId);

        when(userRepo.findById(requesterId)).thenReturn(Optional.of(requester));
        when(svcRepo.findById(serviceOfferingId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> ticketService.create(request));

        assertEquals("Service offering not found", exception.getMessage());
        verify(userRepo).findById(requesterId);
        verify(svcRepo).findById(serviceOfferingId);
        verifyNoInteractions(ticketRepo);
    }
}