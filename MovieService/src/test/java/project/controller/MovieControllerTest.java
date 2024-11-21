package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.MovieApplication;
import project.controllers.MovieController;
import project.controllers.TMDBControllerProxy;
import project.entities.Actor;
import project.entities.Genre;
import project.entities.Movie;
import project.services.ActorService;
import project.services.GenreService;
import project.services.MovieService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@ContextConfiguration(classes = MovieApplication.class)
@ExtendWith(SpringExtension.class)
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;
    @MockBean
    private ActorService actorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private TMDBControllerProxy tmdbControllerProxy;
    private static final int UUID = 0;
    @Autowired
    private ObjectMapper objectMapper;
    private Actor actor;
    private Genre genre;
    private Movie movie;

    @BeforeEach
    public void init() {
        actor = Actor.builder()
                .uuid(UUID)
                .build();

        genre = Genre.builder()
                .uuid(UUID)
                .build();

        movie = Movie.builder()
                .uuid(UUID)
                .actors(List.of(actor))
                .genres(List.of(genre))
                .build();
    }

    @Test
    public void saveMovie_returnsMovieSaved() throws Exception {
        Mockito.when(movieService.saveMovie(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/project/movies/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid", is(UUID)));
    }

    @Test
    public void saveMovie_returnsMovieAlreadyExists() throws Exception {
        Mockito.when(movieService.getMovieByUuid(UUID)).thenReturn(movie);

        mockMvc.perform(post("/project/movies/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Movie with id " + UUID + " already exists"));
    }

    @Test
    public void getAllMovies_returnsMovies() throws Exception {
        Mockito.when(movieService.getAllMovies()).thenReturn(List.of(movie));

        mockMvc.perform(get("/project/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].uuid", is(UUID)));
    }

    @Test
    public void getMovieByUuid_returnsFound() throws Exception {
        Mockito.when(movieService.getMovieByUuid(UUID)).thenReturn(movie);

        mockMvc.perform(get("/project/movies/movie/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(UUID)));
    }

    @Test
    public void getMovieByUuid_returnsNotFound() throws Exception {
        mockMvc.perform(get("/project/movies/movie/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("Movie with id " + UUID + " doesn't exist"));
    }

    @Test
    public void deletedMovie_returnsMovieDeleted() throws Exception {
        Mockito.when(movieService.getMovieByUuid(UUID)).thenReturn(movie);

        mockMvc.perform(delete("/project/movies/movie/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid", is(UUID)));
    }

    @Test
    public void deletedMovie_returnsMovieNotFound() throws Exception {
        mockMvc.perform(delete("/project/movies/movie/" + UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(content().string("Movie with id " + UUID + " doesn't exist"));
    }
}
