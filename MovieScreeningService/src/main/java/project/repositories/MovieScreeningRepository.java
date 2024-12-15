package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.MovieHall;
import project.entities.MovieScreening;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MovieScreeningRepository extends JpaRepository<MovieScreening, Integer> {
    Optional<MovieScreening> findMovieScreeningByUuid(int uuid);
    Optional<MovieScreening> findMovieScreeningByDateAndTimeAndMovieHall(LocalDate date, LocalTime time, MovieHall movieHall);
    Optional<List<MovieScreening>> findMovieScreeningsByDate(LocalDate date);
}
