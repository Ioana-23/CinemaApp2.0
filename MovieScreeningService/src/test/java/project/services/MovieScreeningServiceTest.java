package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.repositories.MovieScreeningRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MovieScreeningServiceTest {
    @InjectMocks
    private MovieScreeningService movieScreeningService;
    @Mock
    private MovieScreeningRepository movieScreeningRepository;
    private static final int UUID = 0;
    private MovieScreening movieScreening;

    @BeforeEach
    public void init() {
        movieScreening = MovieScreening.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void getMovieScreeningByUuid_returnsMovieScreening() {
        Mockito.when(movieScreeningRepository.findMovieScreeningByUuid(UUID)).thenReturn(Optional.of(movieScreening));

        MovieScreening movieScreeningFound = movieScreeningService.getMovieScreeningByUuid(UUID);

        assertNotNull(movieScreeningFound);
        assertEquals(movieScreeningFound.getUuid(), UUID);
    }

    @Test
    public void saveMovieScreening_returnsMovieScreening() {
        Mockito.when(movieScreeningRepository.save(movieScreening)).thenReturn(movieScreening);

        MovieScreening movieScreeningSaved = movieScreeningService.saveMovieScreening(movieScreening);

        assertNotNull(movieScreeningSaved);
        assertEquals(movieScreeningSaved.getUuid(), UUID);
    }

    @Test
    public void getMovieScreeningByDateAndTimeAndMovieHall_returnsMovieScreening() {
        Mockito.when(movieScreeningRepository.findMovieScreeningByDateAndTimeAndMovieHall(any(LocalDate.class), any(LocalTime.class), any(MovieHall.class))).thenReturn(Optional.of(movieScreening));

        MovieScreening movieScreeningFound = movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(LocalDate.now(), LocalTime.now(), MovieHall.builder().build());

        assertNotNull(movieScreeningFound);
        assertEquals(movieScreeningFound.getUuid(), UUID);
    }
}
