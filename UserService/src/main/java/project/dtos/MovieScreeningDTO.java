package project.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieScreeningDTO {
    private int id;
    @JsonIgnore
    private int movie_id;
    private LocalDate date;
    private LocalTime time;
    @JsonIgnore
    private Object movieHall;
    private int uuid;
}
