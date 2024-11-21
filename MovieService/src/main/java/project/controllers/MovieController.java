package project.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dtos.MovieDTO;
import project.entities.Actor;
import project.entities.Genre;
import project.entities.Movie;
import project.services.ActorService;
import project.services.GenreService;
import project.services.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("project/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final GenreService genreService;
    private final ActorService actorService;
    private final ModelMapper modelMapper;
    private final TMDBControllerProxy tmdbControllerProxy;

    @GetMapping("/api")
    public ResponseEntity<List<Movie>> getAllApiExternalApiMovies() throws IOException, InterruptedException {
        tmdbControllerProxy.setModelMapper(modelMapper);
        List<Movie> movies = tmdbControllerProxy.getGeneralMovieInfo();

        for (Movie movie : movies) {
            movie.setActors(tmdbControllerProxy.getMovieActorsInfo(movie));
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @PostMapping("/movie")
    public ResponseEntity<Object> saveMovie(@RequestBody MovieDTO movieDTO) {
        Movie movieToSave = modelMapper.map(movieDTO, Movie.class);
        Movie movieAlreadyExists = movieService.getMovieByUuid(movieToSave.getUuid());
        if (movieAlreadyExists == null) {
            saveActorsInfo(movieToSave);
            saveGenreInfo(movieToSave);
            return new ResponseEntity<>(movieService.saveMovie(movieToSave), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Movie with id " + movieToSave.getUuid() + " already exists", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<Object> getMovieByUuid(@PathVariable int id) {
        Movie movieFound = movieService.getMovieByUuid(id);
        if (movieFound != null) {
            return new ResponseEntity<>(movieFound, HttpStatus.OK);
        }
        return new ResponseEntity<>("Movie with id " + id + " doesn't exist", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/movie/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable int id) {
        Movie movieFound = movieService.getMovieByUuid(id);
        if (movieFound != null) {
            saveActorsInfo(movieFound);
            saveGenreInfo(movieFound);
            movieService.deleteMovieByUuid(id);
            deleteActors(movieFound);
            deleteGenres(movieFound);
            return new ResponseEntity<>(movieFound, HttpStatus.OK);
        }
        return new ResponseEntity<>("Movie with id " + id + " doesn't exist", HttpStatus.NO_CONTENT);
    }

    private void deleteGenres(Movie movieFound) {
        for (Genre genre : movieFound.getGenres()) {
            Genre genreAux = genreService.getGenreByUuid(genre.getUuid());
            if (genreAux != null) {
                List<Movie> moviesWithGenre = movieService.getMoviesByGenre(List.of(genreAux));
                if (moviesWithGenre.isEmpty()) {
                    genreService.removeGenreByUuid(genre.getUuid());
                }
            }
        }
    }

    private void deleteActors(Movie movieFound) {
        for (Actor actor : movieFound.getActors()) {
            Actor actorAux = actorService.getActorByUuid(actor.getUuid());
            if (actorAux != null) {
                List<Movie> moviesWithActor = movieService.getMoviesByActor(List.of(actorAux));
                if (moviesWithActor.isEmpty()) {
                    actorService.removeActorByUuid(actor.getUuid());
                }
            }
        }
    }

    private void saveGenreInfo(Movie movieFound) {
        List<Genre> genres = new ArrayList<>(Collections.nCopies(movieFound.getGenres().size(), null));
        Collections.copy(genres, movieFound.getGenres());
        for (int i = 0, j = 0; i < genres.size(); i++, j++) {
            Genre genreAux = genreService.getGenreByUuid(genres.get(i).getUuid());
            if (genreAux != null) {
                movieFound.getGenres().remove(j);
                movieFound.getGenres().add(genreAux);
                j--;
            }
        }
    }

    private void saveActorsInfo(Movie movieFound) {
        List<Actor> actors = new ArrayList<>(Collections.nCopies(movieFound.getActors().size(), null));
        Collections.copy(actors, movieFound.getActors());
        for (int i = 0, j = 0; i < actors.size(); i++, j++) {
            Actor actorAux = actorService.getActorByUuid(actors.get(i).getUuid());
            if (actorAux != null) {
                movieFound.getActors().remove(j);
                movieFound.getActors().add(actorAux);
                j--;
//                actor = actorAux;
//                actor.setId(actorAux.getId());
            }
        }
    }
}
