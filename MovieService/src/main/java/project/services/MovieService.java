package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Movie;
import project.repositories.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie findMovieByUuid(int id) {
        Optional<Movie> movieFound = movieRepository.findByUuid(id);
        return movieFound.orElse(null);
    }

    @Transactional
    public void removeMovieByUuid(int id) {
        movieRepository.deleteByUuid(id);
    }
}
