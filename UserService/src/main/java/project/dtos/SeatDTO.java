package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeatDTO {
    private int id;
    private int row_number;
    private int seat_number;
    private int movie_hall_uuid;
    private int uuid;
    private boolean available;
}
