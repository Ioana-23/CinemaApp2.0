package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dtos.MovieScreeningDTO;
import project.dtos.MovieDTO;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.services.MovieHallService;
import project.services.MovieScreeningService;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@RestController
@RequestMapping("project/movie_screenings")
@RequiredArgsConstructor
public class MovieScreeningController {
    private final MovieScreeningService movieScreeningService;
    private final MovieHallService movieHallService;
    private final ModelMapper modelMapper;

    @PostMapping("/movie_screening")
    public ResponseEntity<MovieScreening> saveMovieScreening(@RequestBody MovieScreeningDTO movieScreeningDTO) {
        try {
            MovieDTO movieFoundDTO = checkIfMovieExists(movieScreeningDTO.getMovie_uuid());
            if (movieFoundDTO == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            MovieHall movieHallFound = movieHallService.findMovieHallByUuid(movieScreeningDTO.getMovieHall_uuid());
            if (movieHallFound == null) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            MovieScreening movieScreeningToSave = modelMapper.map(movieScreeningDTO, MovieScreening.class);
//            movieScreeningToSave.setMovie(movieFoundDTO);
            movieScreeningToSave.setMovieHall(movieHallFound);
            MovieScreening movieScreeningAlreadyExists = movieScreeningService.findMovieScreeningByUuid(movieScreeningToSave.getUuid());
            if (movieScreeningAlreadyExists == null) {
                return new ResponseEntity<>(movieScreeningService.saveMovieScreening(movieScreeningToSave), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(movieScreeningToSave, HttpStatus.ACCEPTED);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private MovieDTO checkIfMovieExists(int uuid) throws IOException, InterruptedException {
        String ip_address;
        if (isRunningInsideDocker()) {
            ip_address = "host.docker.internal";
        } else {
            ip_address = getIpAddress();
        }
        HttpResponse<String> response = make_call("http://" + ip_address + ":8081/project/movies/movie/" + uuid);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (response.body().isBlank()) {
            return null;
        }
        return mapper.readValue(response.body(), MovieDTO.class);
    }

    public static Boolean isRunningInsideDocker() {

        try (Stream<String> stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

    private static String getIpAddress() throws UnknownHostException, SocketException {
        String ip_address;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip_address = socket.getLocalAddress().getHostAddress();
        }
        return ip_address;
    }

    private HttpResponse<String> make_call(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
//                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjMDczYzc2NzQ5MDk0MTQ4NGRiNDhkMzQ3N2FlOTQ5YyIsIm5iZiI6MTczMTQyODk4OS44NDYxMzU5LCJzdWIiOiI2NzMyMTM1MDNhZjhlYjNlMzVhYTI2NDkiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.DTaeru_ecPfxZ5hGGiDuKrjzkn8C6Lu7qlNOgfuflQo")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @PutMapping("/movie_screening/{id}")
    public ResponseEntity<MovieScreening> updateMovieScreening(@RequestBody MovieScreeningDTO movieScreeningDTO, @PathVariable int id) {
        MovieScreening movieScreeningToUpdate = movieScreeningService.findMovieScreeningByUuid(id);
        if (movieScreeningToUpdate == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        MovieScreening movieScreeningFinal = modelMapper.map(movieScreeningDTO, MovieScreening.class);
        MovieHall movieHallToUpdate = movieHallService.findMovieHallByUuid(movieScreeningDTO.getMovieHall_uuid());
        if (movieHallToUpdate == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        movieScreeningFinal.setMovieHall(movieHallToUpdate);
        movieScreeningService.updateMovieScreening(movieScreeningToUpdate, movieScreeningFinal);
        return new ResponseEntity<>(movieScreeningFinal, HttpStatus.OK);
    }

    @GetMapping("/movie_screening/{id}")
    public ResponseEntity<MovieScreening> findMovieScreeningByUuid(@PathVariable int id) {
        MovieScreening movieScreeningFound = movieScreeningService.findMovieScreeningByUuid(id);
        if (movieScreeningFound == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movieScreeningFound, HttpStatus.OK);
    }
}
