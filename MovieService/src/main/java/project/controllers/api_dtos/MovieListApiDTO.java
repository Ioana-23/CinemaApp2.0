package project.controllers.api_dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieListApiDTO {
    private Object dates;
    private List<MovieApiDTO> results;
    @JsonIgnore
    private int page;
    private int total_pages;
    @JsonIgnore
    private int total_results;
}

