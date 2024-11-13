package project.controllers.api_dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieApiDTO {
    private boolean adult;
    @JsonIgnore
    private String backdrop_path;
    private List<Integer> genre_ids;
    @JsonProperty("id")
    private int uuid;
    @JsonProperty("original_language")
    private String language;
    @JsonIgnore
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    @JsonProperty("release_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String title;
    @JsonIgnore
    private boolean video;
    @JsonIgnore
    private Double vote_average;
    @JsonIgnore
    private int vote_count;
}
