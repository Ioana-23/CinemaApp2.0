package project.controllers.api_dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActorApiDTO {
    @JsonIgnore
    private boolean adult;
    private int gender;
    @JsonProperty("id")
    private int uuid;
    @JsonIgnore
    private String known_for_department;
    @JsonIgnore
    private String original_name;
    private String name;
    private double popularity;
    private String profile_path;
    private int cast_id;
    private String character;
    private String credit_id;
    private int order;
}
