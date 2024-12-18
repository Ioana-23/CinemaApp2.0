package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(name = "movies")
public class Movie {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToMany
    @Cascade({CascadeType.PERSIST})
    @JoinTable(name = "movie_actors", joinColumns = @JoinColumn(name = "movie_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "actors_id", nullable = false))
    private List<Actor> actors;

    @Column(name = "overview", nullable = false)
    private String overview;

    @Column(name = "adult", columnDefinition = "boolean default false", nullable = false)
    private boolean adult;

    @Column(name = "language", nullable = false)
    private String language;

    @ManyToMany
    @Cascade({CascadeType.PERSIST})
    @JoinTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "genre_id",  nullable = false))
    private List<Genre> genres;

    @Column(name = "uuid", unique = true, nullable = false)
    private int uuid;

    @Column(name = "poster_path")
    private String poster_path;
}
