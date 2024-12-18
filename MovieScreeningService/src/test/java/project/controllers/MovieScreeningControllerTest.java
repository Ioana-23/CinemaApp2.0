package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.dtos.MovieDTO;
import project.dtos.MovieInfoDTO;
import project.dtos.movie_screening.MovieScreeningDTO;
import project.dtos.movie_screening.SaveMovieScreeningDTO;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.services.MovieHallService;
import project.services.MovieScreeningService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MovieScreeningController.class)
@ContextConfiguration(classes = MovieScreeningApplication.class)
@ExtendWith(SpringExtension.class)
public class MovieScreeningControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieScreeningService movieScreeningService;
    @MockBean
    private MovieHallService movieHallService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MovieControllerProxy movieControllerProxy;
    private SaveMovieScreeningDTO saveMovieScreeningDTO;
    private MovieInfoDTO movieInfoDTO;
    private MovieScreeningDTO movieScreeningDTO;
    private MovieScreening movieScreening;
    private static final int UUID = 0;
    private MovieHall movieHall;

    @BeforeEach
    public void init() {
        movieInfoDTO = MovieInfoDTO.builder()
                .uuid(UUID)
                .build();

        movieHall = MovieHall.builder()
                .uuid(UUID)
                .build();

        movieScreeningDTO = MovieScreeningDTO.builder()
//                .datetime(List.of(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))))
                .date(LocalDate.now())
                .times(List.of(LocalDateTime.now()))
                .movie_uuid(UUID)
                .uuid(List.of(UUID))
                .movieHall_uuid(List.of(UUID))
                .build();

        movieScreening = MovieScreening.builder()
                .date(LocalDate.now())
                .time(LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))))
                .uuid(UUID)
                .movie_uuid(UUID)
                .movieHall(movieHall)
                .build();

        saveMovieScreeningDTO = SaveMovieScreeningDTO.builder()
                .date(LocalDate.now())
                .times(List.of(LocalDateTime.now()))
                .movie_uuid(UUID)
                .movieHall_uuid(List.of(UUID))
                .build();
    }

    @Test
    public void saveMovieScreening_returnsNoMovieFound() throws Exception {
        mockMvc.perform(post("/project/movie_screenings/movie_screening")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Movie with id " + UUID + " doesn't exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void saveMovieScreening_returnsNoMovieHallFound() throws Exception {
        Mockito.when(movieControllerProxy.getMovieByUuid(UUID)).thenReturn(movieInfoDTO);
        mockMvc.perform(post("/project/movie_screenings/movie_screening")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveMovieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseType", is("ERROR")))
                .andExpect(jsonPath("$.message", is("Movie hall with id " + UUID + " doesn't exist")));
    }

    @Test
    public void saveMovieScreening_returnsMovieScreeningSaved() throws Exception {
        Mockito.when(movieControllerProxy.getMovieByUuid(UUID)).thenReturn(movieInfoDTO);
        Mockito.when(movieHallService.getMovieHallByUuid(UUID)).thenReturn(movieHall);
        Mockito.when(movieScreeningService.saveMovieScreening(any(MovieScreening.class))).thenReturn(movieScreening);

        mockMvc.perform(post("/project/movie_screenings/movie_screening")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveMovieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseObject.uuid[0]", is(UUID)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void saveMovieScreening_returnsMovieScreeningAlreadyExists() throws Exception {
        Mockito.when(movieControllerProxy.getMovieByUuid(UUID)).thenReturn(movieInfoDTO);
        Mockito.when(movieHallService.getMovieHallByUuid(UUID)).thenReturn(movieHall);
        Mockito.when(movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(any(LocalDate.class), any(LocalTime.class), any(MovieHall.class))).thenReturn(movieScreening);

        mockMvc.perform(post("/project/movie_screenings/movie_screening")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Movie screening on " + movieScreening.getDate() + " at " + movieScreening.getTime() + " in movie hall with id " + UUID + " already exists\n")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void updateMovieScreening_returnsMovieScreeningNotFound() throws Exception {
        mockMvc.perform(put("/project/movie_screenings/movie_screening/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Movie screening with " + UUID + " doesn't exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void updateMovieScreening_returnsMovieHallNotFound() throws Exception {
        Mockito.when(movieScreeningService.getMovieScreeningByUuid(UUID)).thenReturn(movieScreening);

        mockMvc.perform(put("/project/movie_screenings/movie_screening/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Movie hall with id " + UUID + " doesn't exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void updateMovieScreening_returnsMovieScreeningAlreadyExists() throws Exception {
        Mockito.when(movieScreeningService.getMovieScreeningByUuid(UUID)).thenReturn(movieScreening);
        Mockito.when(movieHallService.getMovieHallByUuid(UUID)).thenReturn(movieHall);
        Mockito.when(movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(any(LocalDate.class), any(LocalTime.class), any(MovieHall.class))).thenReturn(movieScreening);

        mockMvc.perform(put("/project/movie_screenings/movie_screening/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Movie screening on " + movieScreening.getDate() + " at " + movieScreening.getTime() + " in movie hall with id " + UUID + " already exists")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void updateMovieScreening_returnsMovieScreeningUpdated() throws Exception {
        Mockito.when(movieScreeningService.getMovieScreeningByUuid(UUID)).thenReturn(movieScreening);
        Mockito.when(movieHallService.getMovieHallByUuid(UUID)).thenReturn(movieHall);

        mockMvc.perform(put("/project/movie_screenings/movie_screening/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieScreeningDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject[0].uuid", is(UUID)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void findMovieScreeningByUuid_returnsNotFound() throws Exception {
        mockMvc.perform(get("/project/movie_screenings/movie_screening/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", is("Movie screening with " + UUID + " doesn't exist")))
                .andExpect(jsonPath("$.responseType", is("ERROR")));
    }

    @Test
    public void findMovieScreeningByUuid_returnsFound() throws Exception {
        Mockito.when(movieScreeningService.getMovieScreeningByUuid(UUID)).thenReturn(movieScreening);

        mockMvc.perform(get("/project/movie_screenings/movie_screening/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.uuid", is(UUID)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void findAllMovieScreenings_returns1MovieWith1MovieScreenings() throws Exception {
        Mockito.when(movieScreeningService.getMovieScreenings()).thenReturn(List.of(movieScreening));

        mockMvc.perform(get("/project/movie_screenings/5/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS", hasSize(1)))
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS[0].uuid", hasSize(1)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void findAllMovieScreenings_returns1MovieWith2MovieScreenings() throws Exception {
        MovieScreening movieScreening1 = MovieScreening.builder()
                .date(LocalDate.now())
                .time(LocalTime.parse(LocalTime.now().withMinute(LocalTime.now().getMinute() + 1).format(DateTimeFormatter.ofPattern("HH:mm"))))
                .uuid(1)
                .movie_uuid(UUID)
                .movieHall(movieHall)
                .build();
        Mockito.when(movieScreeningService.getMovieScreenings()).thenReturn(List.of(movieScreening, movieScreening1));

        mockMvc.perform(get("/project/movie_screenings/5/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS", hasSize(1)))
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS[0].uuid", hasSize(2)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void findAllMovieScreenings_returns2MoviesWith2MovieScreenings() throws Exception {
        MovieScreening movieScreening1 = MovieScreening.builder()
                .date(LocalDate.now())
                .time(LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))))
                .uuid(1)
                .movie_uuid(1)
                .movieHall(movieHall)
                .build();
        Mockito.when(movieScreeningService.getMovieScreenings()).thenReturn(List.of(movieScreening, movieScreening1));

        mockMvc.perform(get("/project/movie_screenings/5/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS", hasSize(2)))
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS[0].uuid", hasSize(1)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }

    @Test
    public void findAllMovieScreenings_returns1MovieWith2MovieScreeningDates() throws Exception {
        MovieScreening movieScreening1 = MovieScreening.builder()
                .date(LocalDate.now().withDayOfMonth(LocalDate.now().getDayOfMonth() + 1))
                .time(LocalTime.parse(LocalTime.now().withMinute(LocalTime.now().getMinute() + 1).format(DateTimeFormatter.ofPattern("HH:mm"))))
                .uuid(1)
                .movie_uuid(UUID)
                .movieHall(movieHall)
                .build();
        Mockito.when(movieScreeningService.getMovieScreenings()).thenReturn(List.of(movieScreening, movieScreening1));

        mockMvc.perform(get("/project/movie_screenings/5/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS", hasSize(2)))
                .andExpect(jsonPath("$.responseObject.movieScreeningDTOS[0].uuid", hasSize(1)))
                .andExpect(jsonPath("$.responseType", is("SUCCESS")));
    }
}
