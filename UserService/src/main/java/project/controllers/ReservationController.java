package project.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dtos.MovieScreeningDTO;
import project.dtos.ReservationDTO;
import project.dtos.SeatDTO;
import project.entities.Reservation;
import project.entities.Ticket;
import project.entities.User;
import project.services.ReservationService;
import project.services.UserService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("project/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MovieControllerProxy movieControllerProxy;

    @GetMapping("/{id}")
    public ResponseEntity<List<Reservation>> getAllReservationsByUser(@PathVariable int id) {
        User userToSearch = userService.getUserByUuid(id);
        if (userToSearch == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservationService.getAllReservationsByUser(userToSearch), HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<Object> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservationToSave = modelMapper.map(reservationDTO, Reservation.class);
        Reservation reservationAlreadyExists = reservationService.getReservationByUuid(reservationToSave.getUuid());
        if (reservationAlreadyExists == null) {
            try {
                String message = checkIfMovieScreeningExists(reservationToSave);
                if(message.isBlank())
                {
                    message = checkIfTicketsAreValid(reservationToSave);
                    if (message.isBlank()) {
                        return new ResponseEntity<>(reservationService.saveReservation(reservationToSave), HttpStatus.CREATED);
                    }
                    if(message.contains("does not exist"))
                    {
                        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
                    }
                    return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
                }
                return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(reservationAlreadyExists, HttpStatus.ACCEPTED);
    }

    private String checkIfTicketsAreValid(Reservation reservationToSave) throws IOException, InterruptedException {
        List<Ticket> tickets = reservationToSave.getTickets();
        if (tickets == null || tickets.isEmpty()) {
            return "There are no tickets associated with this reservation";
        }
        return checkIfSeatsAreValid(tickets);
    }

    private String checkIfSeatsAreValid(List<Ticket> tickets) throws IOException, InterruptedException {
        for (Ticket ticket : tickets) {
            SeatDTO seatFound = movieControllerProxy.getSeatByUuid(ticket.getSeat_id());
            if (seatFound == null) {
                return "Seat with id " + ticket.getSeat_id() + " does not exist";
            }
            if (!seatFound.isAvailable()) {
                return "Seat with id " + ticket.getSeat_id() + " is not available";
            }
        }
        return "";
    }

    private String checkIfMovieScreeningExists(Reservation reservationToSave) throws IOException, InterruptedException {
        MovieScreeningDTO movieScreening = movieControllerProxy.getMovieScreeningByUuid(reservationToSave.getMovie_screening_uuid());
        if (movieScreening == null) {
            return "Movie screening with id " + reservationToSave.getMovie_screening_uuid() + " does not exist";
        }
        return "";
    }

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable int id) {
        Reservation reservationToDelete = reservationService.getReservationByUuid(id);
        if (reservationToDelete == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        reservationService.deleteReservationByUuid(id);
        return new ResponseEntity<>(reservationToDelete, HttpStatus.OK);
    }
}
