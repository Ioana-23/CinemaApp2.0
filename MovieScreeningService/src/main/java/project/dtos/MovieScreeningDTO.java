package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class MovieScreeningDTO {
    private int id;
    private int movie_uuid;
    private LocalDate date;
    private LocalTime time;
    private int movieHall_uuid;
    private int uuid;
}
