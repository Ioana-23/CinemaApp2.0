package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.MovieScreeningApplication;
import project.entities.MovieHall;
import project.entities.MovieScreening;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = MovieScreeningApplication.class)
@DirtiesContext
public class MovieScreeningRepositoryTest {
    @Autowired
    private MovieScreeningRepository movieScreeningRepository;
    @Autowired
    private MovieHallRepository movieHallRepository;
    private MovieHall movieHall;
    private MovieScreening movieScreening;
    private static final int UUID = 0;
    private LocalDate date;
    private LocalTime time;

    @BeforeEach
    public void init() {
        date = LocalDate.of(2024, 11, 21);
        time = LocalTime.of(22, 52);

        movieHall = MovieHall.builder()
                .uuid(UUID)
                .build();

        movieScreening = MovieScreening.builder()
                .movie_uuid(UUID)
                .date(date)
                .time(time)
                .movieHall(movieHall)
                .uuid(UUID)
                .build();
    }

    @Test
    public void findMovieScreeningByUuid_returnsFound() {
        movieHallRepository.save(movieHall);
        movieScreeningRepository.save(movieScreening);

        MovieScreening movieScreeningFound = movieScreeningRepository.findMovieScreeningByUuid(UUID).orElse(null);

        assertNotNull(movieScreeningFound);
        assertEquals(movieScreeningFound.getUuid(), UUID);
    }

    @Test
    public void findMovieScreeningByUuid_returnsNotFound() {
        MovieScreening movieScreeningFound = movieScreeningRepository.findMovieScreeningByUuid(UUID).orElse(null);

        assertNull(movieScreeningFound);
    }

    @Test
    public void findMovieScreeningByDateAndTimeAndMovieHall_returnsFound() {
        movieHallRepository.save(movieHall);
        movieScreeningRepository.save(movieScreening);

        MovieScreening movieScreeningFound = movieScreeningRepository.findMovieScreeningByDateAndTimeAndMovieHall(date, time, movieHall).orElse(null);

        assertNotNull(movieScreeningFound);
        assertEquals(movieScreeningFound.getUuid(), UUID);
    }

    @Test
    public void findMovieScreeningByDateAndTimeAndMovieHall_returnsNotFound() {
        movieHallRepository.save(movieHall);
        movieScreening.setTime(LocalTime.of(22, 53));
        movieScreeningRepository.save(movieScreening);

        MovieScreening movieScreeningFound = movieScreeningRepository.findMovieScreeningByDateAndTimeAndMovieHall(date, time, movieHall).orElse(null);

        assertNull(movieScreeningFound);
    }
}
