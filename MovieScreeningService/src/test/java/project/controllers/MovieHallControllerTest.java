package project.controllers;

import org.junit.jupiter.api.BeforeEach;
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
import project.MovieScreeningApplication;
import project.entities.MovieHall;
import project.entities.Seat;
import project.services.MovieHallService;
import project.services.SeatService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MovieHallController.class)
@ContextConfiguration(classes = MovieScreeningApplication.class)
@ExtendWith(SpringExtension.class)
public class MovieHallControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieHallService movieHallService;

    @MockBean
    private SeatService seatService;

    private MovieHall movieHall;
    private Seat seat;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        movieHall = MovieHall.builder()
                .uuid(UUID)
                .build();

        seat = Seat.builder()
                .movieHall(movieHall)
                .uuid(UUID)
                .build();
    }

    @Test
    public void getAllMovieHalls_returnsmovieHalls() throws Exception {
        Mockito.when(movieHallService.getAllMovieHalls()).thenReturn(List.of(movieHall));

        mockMvc.perform(get("/project/movie_halls")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject", hasSize(1)))
                .andExpect(jsonPath("$.responseObject[0].uuid", is(UUID)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void getAllMovieHallSeats_returnsNoMovieHallFound() throws Exception {
        mockMvc.perform(get("/project/movie_halls/movie_hall/" + UUID + "/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Movie hall with id " + UUID + " does not exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void getAllMovieHallSeats_returnsSeats() throws Exception {
        Mockito.when(movieHallService.getMovieHallByUuid(UUID)).thenReturn(movieHall);
        Mockito.when(seatService.getSeatsByMovieHall(any(MovieHall.class))).thenReturn(List.of(seat));

        mockMvc.perform(get("/project/movie_halls/movie_hall/" + UUID + "/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject", hasSize(1)))
                .andExpect(jsonPath("$.responseObject[0].uuid", is(UUID)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }
}
