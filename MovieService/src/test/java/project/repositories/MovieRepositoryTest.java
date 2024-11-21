package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.MovieApplication;
import project.entities.Actor;
import project.entities.Gender;
import project.entities.Genre;
import project.entities.Movie;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = MovieApplication.class)
@DirtiesContext
public class MovieRepositoryTest {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private GenreRepository genreRepository;
    private Movie movie;
    private Actor actor;
    private Genre genre;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        actor = Actor.builder()
                .gender(Gender.OTHER)
                .name("")
                .uuid(UUID)
                .build();

        genre = Genre.builder()
                .name("")
                .uuid(UUID)
                .build();

        actorRepository.save(actor);
        genreRepository.save(genre);

        movie = Movie.builder()
                .adult(false)
                .date(LocalDate.now())
                .title("")
                .language("eng")
                .overview("")
                .actors(List.of(actor))
                .genres(List.of(genre))
                .uuid(UUID)
                .build();
    }

    @Test
    public void findMovieByUuid_returnsFound() {
        movieRepository.save(movie);

        Movie movieFound = movieRepository.findByUuid(UUID).orElse(null);

        assertNotNull(movieFound);
        assertEquals(movieFound.getUuid(), UUID);
    }

    @Test
    public void findMovieByUuid_returnsNotFound() {
        Movie movieFound = movieRepository.findByUuid(UUID).orElse(null);

        assertNull(movieFound);
    }

    @Test
    public void deleteByUuid_returnsDeleted() {
        movieRepository.save(movie);

        Movie movieFound = movieRepository.findByUuid(UUID).orElse(null);

        assertNotNull(movieFound);

        movieRepository.deleteByUuid(UUID);

        movieFound = movieRepository.findByUuid(UUID).orElse(null);

        assertNull(movieFound);
    }

    @Test
    public void findMovieByActors_returnsFound() {
        movieRepository.save(movie);

        List<Movie> moviesFound = movieRepository.findMoviesByActors(List.of(actor)).orElse(null);

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 1);
        assertEquals(moviesFound.getFirst().getUuid(), UUID);
    }

    @Test
    public void findMovieByActors_returnsNotFound() {
        movie.setActors(List.of());
        movieRepository.save(movie);

        List<Movie> moviesFound = movieRepository.findMoviesByActors(List.of(actor)).orElse(null);

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 0);
    }

    @Test
    public void findMovieByGenres_returnsFound() {
        movieRepository.save(movie);

        List<Movie> moviesFound = movieRepository.findMoviesByGenres(List.of(genre)).orElse(null);

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 1);
        assertEquals(moviesFound.getFirst().getUuid(), UUID);
    }

    @Test
    public void findMovieByGenre_returnsNotFound() {
        movie.setGenres(List.of());
        movieRepository.save(movie);

        List<Movie> moviesFound = movieRepository.findMoviesByGenres(List.of(genre)).orElse(null);

        assertNotNull(moviesFound);
        assertEquals(moviesFound.size(), 0);
    }

}
