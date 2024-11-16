package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Actor;
import project.entities.Genre;
import project.entities.Movie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Optional<Movie> findByUuid(int uuid);

    void deleteByUuid(int uuid);
    Optional<List<Movie>> findMoviesByActors(List<Actor> actors);
    Optional<List<Movie>> findMoviesByGenres(List<Genre> genres);
}
