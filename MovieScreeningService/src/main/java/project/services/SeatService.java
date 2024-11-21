package project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Seat;
import project.repositories.SeatRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;

    public Seat getSeatByUuid(int id) {
        Optional<Seat> seatFound = seatRepository.findSeatByUuid(id);
        return seatFound.orElse(null);
    }
}
