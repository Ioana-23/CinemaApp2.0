package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Reservation;
import project.entities.User;
import project.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public List<Reservation> getAllReservationsByUser(User user) {
        Optional<List<Reservation>> reservations = reservationRepository.findReservationsByUser(user);
        return reservations.orElse(null);
    }

    public Reservation getReservationByUuid(int id) {
        Optional<Reservation> reservation = reservationRepository.findReservationByUuid(id);
        return reservation.orElse(null);
    }

    public Reservation saveReservation(Reservation reservationToSave) {
        return reservationRepository.save(reservationToSave);
    }

    @Transactional
    public void deleteReservationByUuid(int id) {
        reservationRepository.deleteByUuid(id);
    }
}
