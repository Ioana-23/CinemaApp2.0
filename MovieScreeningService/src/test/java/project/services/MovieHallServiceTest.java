package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.MovieHall;
import project.repositories.MovieHallRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MovieHallServiceTest {
    @InjectMocks
    private MovieHallService movieHallService;
    @Mock
    private MovieHallRepository movieHallRepository;
    private static final int UUID = 0;
    private MovieHall movieHall;

    @BeforeEach
    public void init() {
        movieHall = MovieHall.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void getMovieHallByUuid_returnsMovieHall() {
        Mockito.when(movieHallRepository.findMovieHallByUuid(UUID)).thenReturn(Optional.of(movieHall));

        MovieHall movieHallFound = movieHallService.getMovieHallByUuid(UUID);

        assertNotNull(movieHallFound);
        assertEquals(movieHallFound.getUuid(), UUID);
    }

    @Test
    public void getAllMovieHalls_returnsMovieHalls() {
        Mockito.when(movieHallRepository.findAll()).thenReturn(List.of(movieHall));

        List<MovieHall> movieHallsFound = movieHallService.getAllMovieHalls();

        assertNotNull(movieHallsFound);
        assertEquals(movieHallsFound.size(), 1);
        assertEquals(movieHallsFound.getFirst().getUuid(), UUID);
    }
}
