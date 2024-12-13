package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.UserApplication;
import project.dtos.MovieScreeningDTO;
import project.dtos.SeatDTO;
import project.entities.Reservation;
import project.entities.Ticket;
import project.entities.User;
import project.entities.UserRole;
import project.services.ReservationService;
import project.services.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@ContextConfiguration(classes = UserApplication.class)
@ExtendWith(SpringExtension.class)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private MovieControllerProxy movieControllerProxy;
    private static final int UUID = 0;
    private static final String EMAIL = "baciuioana23@gmail.com";
    private User user;
    private Reservation reservation;
    private Ticket ticket;
    @Autowired
    private ObjectMapper objectMapper;
    private MovieScreeningDTO movieScreeningDTO;
    private SeatDTO seatDTO1;

    @BeforeEach
    public void init() {
        movieScreeningDTO = MovieScreeningDTO.builder()
                .uuid(UUID)
                .build();

        seatDTO1 = SeatDTO.builder()
                .available(true)
                .uuid(UUID)
                .build();

        user = User.builder()
                .user_role(UserRole.ADMIN)
                .email(EMAIL)
                .uuid(UUID)
                .build();

        ticket = Ticket.builder()
                .seat_uuid(UUID)
                .build();

        reservation = Reservation.builder()
                .user(user)
                .uuid(UUID)
                .movie_screening_uuid(UUID)
                .tickets(List.of(ticket))
                .build();
    }

    @Test
    public void getAllReservationByUsers_returnsReservations() throws Exception {
        Mockito.when(userService.getUserByUuid(UUID)).thenReturn(user);
        Mockito.when(reservationService.getAllReservationsByUser(user)).thenReturn(List.of(reservation));

        mockMvc.perform(get("/project/reservations/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject", hasSize(1)))
                .andExpect(jsonPath("$.responseObject[0].user.email", is(EMAIL)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void getAllReservationByUsers_returnsUserNotFound() throws Exception {
        Mockito.when(reservationService.getAllReservationsByUser(user)).thenReturn(List.of(reservation));

        mockMvc.perform(get("/project/reservations/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("User with id " + UUID + " does not exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void saveReservation_returnsReservationSaved() throws Exception {
        Mockito.when(movieControllerProxy.getMovieScreeningByUuid(reservation.getMovie_screening_uuid())).thenReturn(movieScreeningDTO);
        Mockito.when(movieControllerProxy.getSeatByUuid(UUID)).thenReturn(seatDTO1);
        Mockito.when(reservationService.saveReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/project/reservations/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseObject.user.email", is(EMAIL)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void saveReservation_returnsNoTickets() throws Exception {
        reservation.setTickets(null);

        Mockito.when(movieControllerProxy.getMovieScreeningByUuid(reservation.getMovie_screening_uuid())).thenReturn(movieScreeningDTO);
        Mockito.when(reservationService.saveReservation(reservation)).thenReturn(reservation);

        mockMvc.perform(post("/project/reservations/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andDo(print())
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.message", is("There are no tickets associated with this reservation")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void saveReservation_returnsSeatNotFound() throws Exception {
        Mockito.when(movieControllerProxy.getMovieScreeningByUuid(reservation.getMovie_screening_uuid())).thenReturn(movieScreeningDTO);
        Mockito.when(reservationService.saveReservation(reservation)).thenReturn(reservation);

        mockMvc.perform(post("/project/reservations/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Seat with id " + UUID + " does not exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void saveReservation_returnsSeatNotAvailable() throws Exception {
        seatDTO1.setAvailable(false);

        Mockito.when(movieControllerProxy.getMovieScreeningByUuid(reservation.getMovie_screening_uuid())).thenReturn(movieScreeningDTO);
        Mockito.when(reservationService.saveReservation(reservation)).thenReturn(reservation);
        Mockito.when(movieControllerProxy.getSeatByUuid(UUID)).thenReturn(seatDTO1);

        mockMvc.perform(post("/project/reservations/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andDo(print())
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.message", is("Seat with id " + UUID + " is not available")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void saveReservation_returnsMovieScreeningNotFound() throws Exception {
        Mockito.when(movieControllerProxy.getMovieScreeningByUuid(reservation.getMovie_screening_uuid())).thenReturn(null);

        mockMvc.perform(post("/project/reservations/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Movie screening with id " + UUID + " does not exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void deleteReservation_returnsReservationDeleted() throws Exception {
        Mockito.when(reservationService.getReservationByUuid(UUID)).thenReturn(reservation);

        mockMvc.perform(delete("/project/reservations/reservation/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.uuid", is(UUID)))
                .andExpect(jsonPath("$.responseObject.user.email", is(EMAIL)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void deleteReservation_returnsReservationNotFound() throws Exception {

        mockMvc.perform(delete("/project/reservations/reservation/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Reservation with id " + UUID + " does not exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }
}
