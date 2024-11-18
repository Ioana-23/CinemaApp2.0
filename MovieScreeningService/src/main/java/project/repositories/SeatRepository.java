package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Seat;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    public Optional<Seat> findSeatByUuid(int uuid);
}
