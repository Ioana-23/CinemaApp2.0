package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "movie_halls")
public class MovieHall {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @ManyToMany
//    @Cascade(CascadeType.PERSIST)
//    @JoinTable(name = "movie_hall_configurations", joinColumns = @JoinColumn(name = "movie_hall_id"), inverseJoinColumns = @JoinColumn(name = "seat_id"))
//    private List<Seat> configuration;

    @Column(name = "uuid", unique = true)
    private int uuid;
}
