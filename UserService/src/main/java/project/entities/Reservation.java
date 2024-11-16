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
@Table(name = "reservations")
public class Reservation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "movie_screening_uuid")
    private int movie_screening_uuid;

    @Column(name = "uuid", unique = true)
    private int uuid;

    @ManyToMany
    @Cascade({CascadeType.PERSIST})
    @JoinTable(name = "reservation_tickets", joinColumns = @JoinColumn(name = "reservation_id"), inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;
}
