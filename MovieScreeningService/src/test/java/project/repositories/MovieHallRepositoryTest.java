package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.MovieScreeningApplication;
import project.entities.MovieHall;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = MovieScreeningApplication.class)
@DirtiesContext
public class MovieHallRepositoryTest {
    @Autowired
    private MovieHallRepository movieHallRepository;

    private MovieHall movieHall;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        movieHall = MovieHall.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void findMovieHallByUuid_returnsFound() {
        movieHallRepository.save(movieHall);

        MovieHall movieHallFound = movieHallRepository.findMovieHallByUuid(UUID).orElse(null);

        assertNotNull(movieHallFound);
        assertEquals(movieHallFound.getUuid(), UUID);
    }

    @Test
    public void findMovieHallByUuid_returnsUserNotFound() {
        MovieHall movieHallFound = movieHallRepository.findMovieHallByUuid(UUID).orElse(null);

        assertNull(movieHallFound);
    }

}
