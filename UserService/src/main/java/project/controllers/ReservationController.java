package project.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controllers.response.Response;
import project.controllers.response.ResponseType;
import project.dtos.*;
import project.entities.Reservation;
import project.entities.Ticket;
import project.entities.User;
import project.services.ReservationService;
import project.services.TicketService;
import project.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("project/reservations")
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MovieControllerProxy movieControllerProxy;
    private final TicketService ticketService;

    private Random random = new Random();

    private int generate_uuid(boolean reservation) {
        if (reservation) {
            int final_uuid;
            do {
                int no_digits = random.nextInt(4, 7);
                final_uuid = 1;
                for (int i = 0; i < no_digits; i++) {
                    int current_digit = random.nextInt(0, 10);
                    final_uuid = final_uuid * 10 + current_digit;
                }
            } while (reservationService.getReservationByUuid(final_uuid) != null);
            return final_uuid;
        }
        int final_uuid;
        do {
            int no_digits = random.nextInt(4, 7);
            final_uuid = 1;
            for (int i = 0; i < no_digits; i++) {
                int current_digit = random.nextInt(0, 10);
                final_uuid = final_uuid * 10 + current_digit;
            }
        } while (ticketService.getTicketByUuid(final_uuid) != null);
        return final_uuid;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getAllReservationsByUser(@PathVariable int id) {
        User userToSearch = userService.getUserByUuid(id);
        if (userToSearch == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .message("User with id " + id + " does not exist")
                            .responseType(ResponseType.ERROR)
                            .build(),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseType(ResponseType.SUCCESS)
                        .responseObject(reservationService.getAllReservationsByUser(userToSearch))
                        .build(),
                HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<Response> saveReservation(@RequestBody SaveReservationDTO saveReservationDTO) {
        Reservation reservationToSave = modelMapper.map(saveReservationDTO, Reservation.class);
        List<Ticket> tickets = new ArrayList<>();
        for (SeatDTO seatDTO : saveReservationDTO.getTickets()) {
            tickets.add(ticketService.saveTicket(Ticket.builder()
                    .seat_uuid(seatDTO.getUuid())
                    .uuid(generate_uuid(false))
                    .build()));
        }
        reservationToSave.setTickets(tickets);
        try {
            User userFound = userService.getUserByUuid(reservationToSave.getUser().getUuid());
            if (userFound != null) {
                reservationToSave.setUser(userFound);
                String message = checkIfMovieScreeningExists(reservationToSave);
                if (message.isBlank()) {
                    message = checkIfTicketsAreValid(reservationToSave);
                    if (message.isBlank()) {
                        reservationToSave.setUuid(generate_uuid(true));
                        return new ResponseEntity<>(
                                Response.builder()
                                        .responseObject(reservationService.saveReservation(reservationToSave))
                                        .responseType(ResponseType.SUCCESS)
                                        .build(),
                                HttpStatus.CREATED);
                    }
                    if (message.contains("does not exist")) {
                        return new ResponseEntity<>(
                                Response.builder()
                                        .responseType(ResponseType.ERROR)
                                        .message(message)
                                        .build(),
                                HttpStatus.NO_CONTENT);
                    }
                    return new ResponseEntity<>(
                            Response.builder()
                                    .message(message)
                                    .responseType(ResponseType.ERROR)
                                    .build(),
                            HttpStatus.EXPECTATION_FAILED);
                }
                return new ResponseEntity<>(
                        Response.builder()
                                .responseType(ResponseType.ERROR)
                                .message(message)
                                .build(),
                        HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(
                        Response.builder()
                                .message("User with uuid: " + reservationToSave.getUser().getUuid() + " does not exist")
                                .responseType(ResponseType.ERROR)
                                .build(),
                        HttpStatus.OK);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
            SeatDTO seatFound = movieControllerProxy.getSeatByUuid(ticket.getSeat_uuid());
            if (seatFound == null) {
                return "Seat with id " + ticket.getSeat_uuid() + " does not exist";
            }
            if (!seatFound.isAvailable()) {
                return "Seat with id " + ticket.getSeat_uuid() + " is not available";
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
    public ResponseEntity<Response> deleteReservation(@PathVariable int id) {
        Reservation reservationToDelete = reservationService.getReservationByUuid(id);
        if (reservationToDelete == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .message("Reservation with id " + id + " does not exist")
                            .responseType(ResponseType.ERROR)
                            .build(),
                    HttpStatus.NO_CONTENT);
        }
        reservationService.deleteReservationByUuid(id);
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(reservationToDelete)
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK);
    }
}
