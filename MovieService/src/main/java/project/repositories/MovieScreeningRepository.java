package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.entities.MovieScreening;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface MovieScreeningRepository extends JpaRepository<MovieScreening, Integer> {
    Optional<MovieScreening> findMovieScreeningByUuid(int uuid);
}
