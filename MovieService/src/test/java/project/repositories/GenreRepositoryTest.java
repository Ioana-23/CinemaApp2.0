package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.MovieApplication;
import project.entities.Genre;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = MovieApplication.class)
@DirtiesContext
public class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;

    private Genre genre;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        genre = Genre.builder()
                .name("")
                .uuid(UUID)
                .build();
    }

    @Test
    public void findGenreByUuid_returnsFound() {
        genreRepository.save(genre);

        Genre genreFound = genreRepository.findByUuid(UUID).orElse(null);

        assertNotNull(genreFound);
        assertEquals(genreFound.getUuid(), UUID);
    }

    @Test
    public void findGenreByUuid_returnsNotFound() {
        Genre genreFound = genreRepository.findByUuid(UUID).orElse(null);

        assertNull(genreFound);
    }

    @Test
    public void deleteGenreByUuid_returnsDeleted() {
        genreRepository.save(genre);

        Genre genreFound = genreRepository.findByUuid(UUID).orElse(null);

        assertNotNull(genreFound);

        genreRepository.deleteByUuid(UUID);

        genreFound = genreRepository.findByUuid(UUID).orElse(null);

        assertNull(genreFound);
    }
}
