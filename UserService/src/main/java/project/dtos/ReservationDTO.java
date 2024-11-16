package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationDTO {
    private int id;
    private UserDTO user;
    private int movie_screening_uuid;
    private int uuid;

}
