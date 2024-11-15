package project.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole user_role;

    @Column(name = "uuid")
    private int uuid;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
