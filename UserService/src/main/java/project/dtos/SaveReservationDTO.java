package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SaveReservationDTO {
    private UserDTO user;
    private int movie_screening_uuid;
    private List<SeatDTO> tickets;
}
