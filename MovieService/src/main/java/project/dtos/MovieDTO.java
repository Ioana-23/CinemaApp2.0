package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDTO {
    private String title;
    private LocalDate date;
    private String poster_path;
    private List<ActorDTO> actors;
    private String overview;
    private boolean adult;
    private String language;
    private List<GenreDTO> genres;
    private int uuid;
}
