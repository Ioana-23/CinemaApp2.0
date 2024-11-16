package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Reservation;
import project.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findReservationByUuid(int uuid);
    Optional<List<Reservation>> findReservationsByUser(User user);
    void deleteByUuid(int uuid);
}
