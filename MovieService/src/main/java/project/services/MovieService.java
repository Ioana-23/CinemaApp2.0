package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Actor;
import project.entities.Genre;
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

    public Movie getMovieByUuid(int id) {
        Optional<Movie> movieFound = movieRepository.findByUuid(id);
        return movieFound.orElse(null);
    }

    @Transactional
    public void deleteMovieByUuid(int id) {
        movieRepository.deleteByUuid(id);
    }

    public List<Movie> getMoviesByActor(List<Actor> actors) {
        Optional<List<Movie>> movies = movieRepository.findMoviesByActors(actors);
        return movies.orElse(null);
    }

    public List<Movie> getMoviesByGenre(List<Genre> genre) {
        Optional<List<Movie>> movies = movieRepository.findMoviesByGenres(genre);
        return movies.orElse(null);
    }
}
