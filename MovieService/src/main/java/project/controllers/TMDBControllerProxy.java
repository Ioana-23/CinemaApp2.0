package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import project.controllers.api_dtos.*;
import project.entities.Actor;
import project.entities.Gender;
import project.entities.Genre;
import project.entities.Movie;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Setter
public class TMDBControllerProxy {
    private ModelMapper modelMapper;
    public List<Movie> getGeneralMovieInfo() throws IOException, InterruptedException {
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

    public List<Actor> getMovieActorsInfo(Movie movie) throws IOException, InterruptedException {
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
}
