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
public class ActorListApiDTO {
    @JsonIgnore
    private int id;
    private List<ActorApiDTO> cast;

    @JsonIgnore
    private String crew;
}
