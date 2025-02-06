package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.entities.TicketType;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TicketDTO {
    private int uuid;
    private int seat_uuid;
}
