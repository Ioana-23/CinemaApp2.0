package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.Actor;
import project.entities.Genre;
import project.entities.Movie;
import project.repositories.MovieRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;
    private static final int UUID = 0;
    private Movie movie;
    private Actor actor;
    private Genre genre;

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
    public void saveMovie_returnsMovie() {
        Mockito.when(movieRepository.save(movie)).thenReturn(movie);

        Movie movieFound = movieService.saveMovie(movie);

        assertNotNull(movieFound);
        assertEquals(movieFound.getUuid(), UUID);
    }

    @Test
    public void getAllMovies_returnsMovies() {
        Mockito.when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<Movie> moviesFound = movieService.getAllMovies();

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 1);
        assertEquals(moviesFound.getFirst().getUuid(), UUID);
    }

    @Test
    public void getMoviesByActor_returnsMovies() {
        Mockito.when(movieRepository.findMoviesByActors(List.of(actor))).thenReturn(Optional.of(List.of(movie)));

        List<Movie> moviesFound = movieService.getMoviesByActor(List.of(actor));

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 1);
        assertEquals(moviesFound.getFirst().getUuid(), UUID);
    }

    @Test
    public void getMoviesByGenres_returnsMovies() {
        Mockito.when(movieRepository.findMoviesByGenres(List.of(genre))).thenReturn(Optional.of(List.of(movie)));

        List<Movie> moviesFound = movieService.getMoviesByGenre(List.of(genre));

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 1);
        assertEquals(moviesFound.getFirst().getUuid(), UUID);
    }
}
