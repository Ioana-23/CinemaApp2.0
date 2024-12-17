package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.entities.Gender;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActorDTO {
    private String name;
    private Gender gender;
    private int uuid;
}
