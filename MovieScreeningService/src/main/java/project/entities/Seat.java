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
@Table(name = "seats")
public class Seat {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "available")
    private boolean available;

    @Column(name = "row_number")
    private int row_number;

    @Column(name = "seat_number")
    private int seat_number;

    @JoinColumn(name = "movie_hall_id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = MovieHall.class)
    private MovieHall movieHall;
}
