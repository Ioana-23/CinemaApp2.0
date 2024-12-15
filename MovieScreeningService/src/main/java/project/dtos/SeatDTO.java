package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatDTO {
    private int uuid;
    private int row_number;
    private int seat_number;
}
