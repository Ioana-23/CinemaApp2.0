package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.Genre;
import project.repositories.GenreRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GenreServiceTest {
    @InjectMocks
    private GenreService genreService;
    @Mock
    private GenreRepository genreRepository;
    private static final int UUID = 0;
    private Genre genre;

    @BeforeEach
    public void init() {
        genre = Genre.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void getGenreByUuid_returnsFound() {
        Mockito.when(genreRepository.findByUuid(UUID)).thenReturn(Optional.of(genre));

        Genre genreFound = genreService.getGenreByUuid(UUID);

        assertNotNull(genreFound);
        assertEquals(genreFound.getUuid(), UUID);
    }
}
