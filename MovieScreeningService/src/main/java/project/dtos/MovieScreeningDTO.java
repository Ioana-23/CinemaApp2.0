package project.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieScreeningDTO {
    private int movie_uuid;
    @JsonIgnore
    private List<LocalDateTime> datetime;
    private int movieHall_uuid;
    @JsonIgnore
    private List<Integer> uuid;
}
