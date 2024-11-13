package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controllers.api_dtos.ActorListApiDTO;
import project.controllers.api_dtos.GenreListApiDTO;
import project.controllers.api_dtos.MovieApiDTO;
import project.controllers.api_dtos.ActorApiDTO;
import project.controllers.api_dtos.ActorListApiDTO;
import project.controllers.api_dtos.GenreListApiDTO;
import project.controllers.api_dtos.GenreApiDTO;
import project.controllers.api_dtos.MovieListApiDTO;
import project.dtos.MovieDTO;
import project.entities.Actor;
import project.entities.Gender;
import project.entities.Genre;
import project.entities.Movie;
import project.services.ActorService;
import project.services.GenreService;
import project.services.MovieService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("project/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    private final GenreService genreService;
    private final ActorService actorService;
    private final ModelMapper modelMapper;

    @GetMapping("/api")
    public ResponseEntity<List<Movie>> getAllApiExternalApiMovies() throws IOException, InterruptedException {
        List<Movie> movies = getGeneralMovieInfo();

        for (Movie movie : movies) {
            movie.setActors(getMovieActorsInfo(movie));
        }

        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    private List<Movie> getGeneralMovieInfo() throws IOException, InterruptedException {
        HttpResponse<String> response = make_call("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MovieListApiDTO map = mapper.readValue(response.body(), MovieListApiDTO.class);
        List<Movie> movies = new ArrayList<>();
        List<Genre> genreList = getGenreApiInfo();
        for (MovieApiDTO movie : map.getResults()) {
            Movie movieAux = modelMapper.map(movie, Movie.class);
            movieAux.setGenres(genreList.stream().filter(genre -> movie.getGenre_ids().contains(genre.getUuid())).toList());
            movies.add(movieAux);
        }
        return movies;
    }

    private List<Actor> getMovieActorsInfo(Movie movie) throws IOException, InterruptedException {
        HttpResponse<String> response = make_call("https://api.themoviedb.org/3/movie/" + movie.getUuid() + "/credits?language=en-US");
        ObjectMapper mapper = new ObjectMapper();
        ActorListApiDTO map = mapper.readValue(response.body(), ActorListApiDTO.class);
        List<Actor> actors = new ArrayList<>();
        map.getCast().sort(Comparator.comparingInt(ActorApiDTO::getOrder));
        for (ActorApiDTO actor : map.getCast().subList(0, Math.min(5, map.getCast().size()))) {
            Actor actorAux = modelMapper.map(actor, Actor.class);
            actorAux.setGender(Gender.values()[actor.getGender()]);
            actors.add(actorAux);
        }
        return actors;
    }

    private List<Genre> getGenreApiInfo() throws IOException, InterruptedException {
        HttpResponse<String> response = make_call("https://api.themoviedb.org/3/genre/movie/list?language=en");
        ObjectMapper mapper = new ObjectMapper();
        GenreListApiDTO map = mapper.readValue(response.body(), GenreListApiDTO.class);
        List<Genre> genreList = new ArrayList<>();
        for (GenreApiDTO genre : map.getGenres()) {
            genreList.add(modelMapper.map(genre, Genre.class));
        }
        return genreList;
    }

    private HttpResponse<String> make_call(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMDczYzc2NzQ5MDk0MTQ4NGRiNDhkMzQ3N2FlOTQ5YyIsIm5iZiI6MTczMTQyODk4OS44NDYxMzU5LCJzdWIiOiI2NzMyMTM1MDNhZjhlYjNlMzVhYTI2NDkiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.DTaeru_ecPfxZ5hGGiDuKrjzkn8C6Lu7qlNOgfuflQo")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @PostMapping("/movie")
    public ResponseEntity<Movie> saveMovie(@RequestBody MovieDTO movieDTO) {
        Movie movieToSave = modelMapper.map(movieDTO, Movie.class);
        Movie movieAlreadyExists = movieService.findMovieByUuid(movieToSave.getUuid());
        if (movieAlreadyExists == null) {
            saveActorsInfo(movieToSave);
            saveGenreInfo(movieToSave);
            return new ResponseEntity<>(movieService.saveMovie(movieToSave), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(movieAlreadyExists, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.ACCEPTED);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<Movie> findMovieByUuid(@PathVariable int id) {
        Movie movieFound = movieService.findMovieByUuid(id);
        if (movieFound != null) {
            return new ResponseEntity<>(movieFound, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/remove/movie/{id}")
    public ResponseEntity<Movie> removeMovieByUuid(@PathVariable int id) {
        Movie movieFound = movieService.findMovieByUuid(id);
        if (movieFound != null) {
            saveActorsInfo(movieFound);
            saveGenreInfo(movieFound);
            movieService.removeMovieByUuid(id);
            deleteActors(movieFound);
            deleteGenres(movieFound);
            return new ResponseEntity<>(movieFound, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    private void deleteGenres(Movie movieFound) {
        for (Genre genre : movieFound.getGenres()) {
            Genre genreAux = genreService.getGenreByUuid(genre.getUuid());
            if (genreAux != null) {
                genreService.removeGenreByUuid(genre.getUuid());
            }
        }
    }

    private void deleteActors(Movie movieFound) {
        for (Actor actor : movieFound.getActors()) {
            Actor actorAux = actorService.getActorByUuid(actor.getUuid());
            if (actorAux != null) {
                actorService.removeActorByUuid(actor.getUuid());
            }
        }
    }

    private void saveGenreInfo(Movie movieFound) {
        for (Genre genre : movieFound.getGenres()) {
            Genre genreAux = genreService.getGenreByUuid(genre.getUuid());
            if (genreAux != null) {
                genre.setId(genreAux.getId());
            }
        }
    }

    private void saveActorsInfo(Movie movieFound) {
        for (Actor actor : movieFound.getActors()) {
            Actor actorAux = actorService.getActorByUuid(actor.getUuid());
            if (actorAux != null) {
                actor.setId(actorAux.getId());
            }
        }
    }
}
