package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.MovieScreening;

import java.util.Optional;

public interface MovieScreeningRepository extends JpaRepository<MovieScreening, Integer> {
    Optional<MovieScreening> findMovieScreeningByUuid(int uuid);
}
