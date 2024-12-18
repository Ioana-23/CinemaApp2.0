package project.dtos.movie_screening;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOfMovieScreeningsDTO {
    private int totalPages;
    private int currentPage;
    private List<MovieScreeningDTO> movieScreeningDTOS;
}
