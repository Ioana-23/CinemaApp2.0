package project.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieInfoDTO {
    private String title;
    private LocalDate date;
    @JsonIgnore
    private Object actors;
    private String overview;
    private boolean adult;
    private String language;
    @JsonIgnore
    private Object genres;
    private int uuid;
}
