package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Reservation;
import project.entities.Ticket;
import project.entities.User;
import project.repositories.ReservationRepository;
import project.repositories.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public Ticket getTicketByUuid(int uuid) {
        Optional<Ticket> ticket = ticketRepository.findTicketByUuid(uuid);
        return ticket.orElse(null);
    }

    public Ticket saveTicket(Ticket ticketToSave) {
        return ticketRepository.save(ticketToSave);
    }

}
