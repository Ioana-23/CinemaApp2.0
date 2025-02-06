package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SeatDTO {
    private int uuid;
    private int row_number;
    private int seat_number;
    private boolean available;
}
