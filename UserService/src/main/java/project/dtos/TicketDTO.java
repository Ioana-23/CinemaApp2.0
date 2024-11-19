package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.entities.TicketType;

@Data
@AllArgsConstructor
public class TicketDTO {
    private int id;
    private int uuid;
    private TicketType ticketType;
    private int seat_uuid;
}
