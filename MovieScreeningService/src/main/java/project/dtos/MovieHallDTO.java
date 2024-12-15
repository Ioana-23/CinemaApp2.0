package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MovieHallDTO {
    private List<MovieHallSeatDTO> configuration;
    private int uuid;
}
