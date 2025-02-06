package project.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.controllers.response.ResponseType;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDTO {
    private String title;
    private LocalDate date;
    private String poster_path;
    @JsonIgnore
    private Object actors;
    private String overview;
    private boolean adult;
    private String language;
    @JsonIgnore
    private Object genres;
    private int uuid;
}
