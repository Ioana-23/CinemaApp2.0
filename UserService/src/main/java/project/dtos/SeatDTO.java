package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.controllers.response.ResponseType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatDTO {
    private int row_number;
    private int seat_number;
    private int movie_hall_uuid;
    private int uuid;
    private boolean available;
}
