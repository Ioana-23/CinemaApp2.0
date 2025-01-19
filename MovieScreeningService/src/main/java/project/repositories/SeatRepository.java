package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.MovieHall;
import project.entities.Seat;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    Optional<Seat> findSeatByUuid(int uuid);
    Optional<List<Seat>> findSeatsByMovieHall(MovieHall movieHall);
}
