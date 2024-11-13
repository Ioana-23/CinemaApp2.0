package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Genre;
import project.repositories.GenreRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre getGenreByUuid(int id) {
        Optional<Genre> genreFound = genreRepository.findByUuid(id);
        return genreFound.orElse(null);
    }

    @Transactional
    public void removeGenreByUuid(int id) {
        genreRepository.deleteByUuid(id);
    }
}
