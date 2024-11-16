package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "movie_screenings")
public class MovieScreening {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "movie_id")
    private int movie_id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @JoinColumn(name = "movie_hall_id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = MovieHall.class)
    private MovieHall movieHall;

    @Column(name = "uuid", unique = true)
    private int uuid;
}
