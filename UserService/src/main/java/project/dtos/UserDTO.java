package project.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import project.entities.UserRole;

@Data
@AllArgsConstructor
public class UserDTO {
    private String first_name;
    private String last_name;
    private UserRole user_role;
    private int uuid;
    private String email;
    private String password;
}
