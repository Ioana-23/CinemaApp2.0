package project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dtos.MovieScreeningDTO;
import project.dtos.ReservationDTO;
import project.entities.Reservation;
import project.entities.User;
import project.services.ReservationService;
import project.services.UserService;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("project/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<List<Reservation>> getAllReservationsByUser(@PathVariable int id) {
        User userToSearch = userService.findUserByUuid(id);
        return new ResponseEntity<>(reservationService.getAllReservationsByUser(userToSearch), HttpStatus.OK);
    }

    @PostMapping("/reservation")
    public ResponseEntity<Reservation> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        Reservation reservationToSave = modelMapper.map(reservationDTO, Reservation.class);
        Reservation reservationAlreadyExists = reservationService.findReservationByUuid(reservationToSave.getUuid());
        if (reservationAlreadyExists == null) {
            try {
                MovieScreeningDTO movieScreening = checkIfMovieHallExists(reservationToSave.getMovie_screening_uuid());
                if (movieScreening == null) {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(reservationService.saveReservation(reservationToSave), HttpStatus.CREATED);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(reservationAlreadyExists, HttpStatus.ACCEPTED);
    }

    public static Boolean isRunningInsideDocker() {

        try (Stream<String> stream =
                     Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }

    private MovieScreeningDTO checkIfMovieHallExists(int uuid) throws IOException, InterruptedException {
        String ip_address;
        if (isRunningInsideDocker()) {
            ip_address = "host.docker.internal";
        } else {
            ip_address = getIpAddress();
        }
        HttpResponse<String> response = make_call("http://" + ip_address + ":8083/project/movie_screenings/movie_screening/" + uuid);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        if (!response.body().isBlank()) {
            return mapper.readValue(response.body(), MovieScreeningDTO.class);
        }
        return null;
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

    @DeleteMapping("/reservation/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable int id) {
        Reservation reservationToDelete = reservationService.findReservationByUuid(id);
        if (reservationToDelete == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        reservationService.deleteReservationByUuid(id);
        return new ResponseEntity<>(reservationToDelete, HttpStatus.OK);
    }
}
