package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class MovieDTO {
    private String title;
    private LocalDate date;
    private List<ActorDTO> actors;
    private String overview;
    private boolean adult;
    private String language;
    private List<GenreDTO> genres;
    private int uuid;
}
