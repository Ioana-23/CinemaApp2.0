package project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.entities.Gender;

@Data
@AllArgsConstructor
public class ActorDTO {
    private int id;
    private String name;
    private Gender gender;
    private int uuid;
}
