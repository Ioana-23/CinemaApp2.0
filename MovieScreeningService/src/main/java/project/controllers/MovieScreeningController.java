package project.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controllers.response.Response;
import project.controllers.response.ResponseType;
import project.dtos.MovieDTO;
import project.dtos.MovieInfoDTO;
import project.dtos.MovieScreeningDTO;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.services.MovieHallService;
import project.services.MovieScreeningService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("project/movie_screenings")
@RequiredArgsConstructor
@CrossOrigin
public class MovieScreeningController {
    private final MovieScreeningService movieScreeningService;
    private final MovieHallService movieHallService;
    private final ModelMapper modelMapper;
    private final MovieControllerProxy movieControllerProxy;

    @PostMapping("/movie_screening")
    public ResponseEntity<Response> saveMovieScreening(@RequestBody MovieScreeningDTO movieScreeningDTO) {
        List<MovieScreening> movieScreeningsToSave = new ArrayList<>();
        MovieScreening movieScreeningToSave = MovieScreening.builder()
                .movie_uuid(movieScreeningDTO.getMovie_uuid())
                .date(movieScreeningDTO.getDate())
                .build();
        String message = "";
        for (int i = 0; i < movieScreeningDTO.getUuid().size(); i++) {
            try {
                movieScreeningToSave.setUuid(movieScreeningDTO.getUuid().get(i));
                movieScreeningToSave.setTime(LocalTime.parse(movieScreeningDTO.getTimes().get(i).format(DateTimeFormatter.ofPattern("HH:mm"))));
                MovieInfoDTO movieFoundDTO = movieControllerProxy.getMovieByUuid(movieScreeningToSave.getMovie_uuid());
                if (movieFoundDTO == null) {
                    return new ResponseEntity<>(
                            Response.builder()
                                    .message("Movie with id " + movieScreeningToSave.getMovie_uuid() + " doesn't exist")
                                    .responseType(ResponseType.ERROR)
                                    .build(),
                            HttpStatus.NO_CONTENT);
                }
                MovieHall movieHallFound = movieHallService.getMovieHallByUuid(movieScreeningDTO.getMovieHall_uuid().get(i));
                if (movieHallFound == null) {
                    return new ResponseEntity<>(
                            Response.builder()
                                    .responseType(ResponseType.ERROR)
                                    .message("Movie hall with id " + movieScreeningDTO.getMovieHall_uuid().get(i) + " doesn't exist")
                                    .build(),
                            HttpStatus.NO_CONTENT);
                }
                movieScreeningToSave.setMovieHall(movieHallFound);
                MovieScreening movieScreeningAlreadyExists = movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(movieScreeningToSave.getDate(), movieScreeningToSave.getTime(), movieHallFound);
                if (movieScreeningAlreadyExists == null) {
                    movieScreeningsToSave.add(movieScreeningService.saveMovieScreening(movieScreeningToSave));
                    continue;
                }
                message = message + "Movie screening on " + movieScreeningToSave.getDate() + " at " + movieScreeningToSave.getTime() + " in movie hall with id " + movieHallFound.getUuid() + " already exists" + "\n";
//                return new ResponseEntity<>(
//                        Response.builder()
//                                .responseType(ResponseType.ERROR)
//                                .message("Movie screening on " + movieScreeningToSave.getDate() + " at " + movieScreeningToSave.getTime() + " in movie hall with id " + movieHallFound.getUuid() + " already exists")
//                                .build(),
//                        HttpStatus.OK);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (movieScreeningsToSave.isEmpty()) {
            return new ResponseEntity<>(
                    Response.builder()
                            .responseType(ResponseType.ERROR)
                            .message(message)
                            .build(),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseType(ResponseType.SUCCESS)
                        .message(message)
                        .responseObject(movieScreeningsToSave)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/movie_screening/{id}")
    public ResponseEntity<Response> updateMovieScreening(@RequestBody MovieScreeningDTO movieScreeningDTO, @PathVariable int id) {
        MovieScreening movieScreeningToUpdate = movieScreeningService.getMovieScreeningByUuid(id);
        if (movieScreeningToUpdate == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .message("Movie screening with " + id + " doesn't exist")
                            .responseType(ResponseType.ERROR)
                            .build(),
                    HttpStatus.NO_CONTENT);
        }
        int index = movieScreeningDTO.getUuid().indexOf(id);
        MovieScreening movieScreeningFinal = MovieScreening.builder()
                .uuid(id)
                .date(movieScreeningDTO.getDate())
                .time(LocalTime.parse(movieScreeningDTO.getTimes().get(index).format(DateTimeFormatter.ofPattern("HH:mm"))))
                .movie_uuid(movieScreeningDTO.getMovie_uuid())
                .build();
        String message = "";
        List<MovieScreening> movieScreeningsUpdated = new ArrayList<>();
        for (int i = 0; i < movieScreeningDTO.getMovieHall_uuid().size(); i++) {
            MovieHall movieHallToUpdate = movieHallService.getMovieHallByUuid(movieScreeningDTO.getMovieHall_uuid().get(i));
            if (movieHallToUpdate == null) {
                message += "Movie hall with id " + movieScreeningDTO.getMovieHall_uuid().get(i) + " doesn't exist";
                continue;
            }
            movieScreeningFinal.setMovieHall(movieHallToUpdate);
            MovieScreening movieScreeningAlreadyExists = movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(movieScreeningFinal.getDate(), movieScreeningFinal.getTime(), movieHallToUpdate);
            if (movieScreeningAlreadyExists == null) {
                movieScreeningService.updateMovieScreening(movieScreeningToUpdate, movieScreeningFinal);
                MovieScreening newMovieScreening = movieScreeningService.getMovieScreeningByUuid(movieScreeningFinal.getUuid());
                movieScreeningsUpdated.add(newMovieScreening);
                continue;
            }
            message += "Movie screening on " + movieScreeningFinal.getDate() + " at " + movieScreeningFinal.getTime() + " in movie hall with id " + movieHallToUpdate.getUuid() + " already exists";
        }
        if (movieScreeningsUpdated.isEmpty()) {
            return new ResponseEntity<>(
                    Response.builder()
                            .responseType(ResponseType.ERROR)
                            .message(message)
                            .build(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(movieScreeningsUpdated)
                        .message(message)
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping("/movie_screening/{id}")
    public ResponseEntity<Response> findMovieScreeningByUuid(@PathVariable int id) {
        MovieScreening movieScreeningFound = movieScreeningService.getMovieScreeningByUuid(id);
        if (movieScreeningFound == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .message("Movie screening with " + id + " doesn't exist")
                            .responseType(ResponseType.ERROR)
                            .build(),
                    HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(movieScreeningFound)
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> getAllMovieScreenings() {
        List<MovieScreening> movieScreeningList = movieScreeningService.getMovieScreenings();
        List<MovieScreeningDTO> movieScreeningDTOS = new ArrayList<>();
        for(int i = 0; i < movieScreeningList.size(); i++)
        {
            List<Integer> preExistingUUIDs = new ArrayList<>(List.of(movieScreeningList.get(i).getUuid()));
            List<Integer> preExistingMovie_halls = new ArrayList<>(List.of(movieScreeningList.get(i).getMovieHall().getUuid()));
            List<LocalDateTime> preExistingTimes = new ArrayList<>(List.of(LocalDateTime.of(movieScreeningList.get(i).getDate(), movieScreeningList.get(i).getTime())));
            for(int j = 0; j < movieScreeningDTOS.size(); j++)
            {
                if(movieScreeningDTOS.get(j).getMovie_uuid() == movieScreeningList.get(i).getMovie_uuid() && movieScreeningDTOS.get(j).getDate().equals(movieScreeningList.get(i).getDate()))
                {
                    preExistingTimes.addAll(movieScreeningDTOS.get(j).getTimes());
                    preExistingMovie_halls.addAll(movieScreeningDTOS.get(j).getMovieHall_uuid());
                    preExistingUUIDs.addAll(movieScreeningDTOS.get(j).getUuid());
                    movieScreeningDTOS.remove(j);
                    break;
                }
            }
            MovieScreeningDTO movieScreeningDTO = MovieScreeningDTO.builder()
                    .uuid(preExistingUUIDs)
                    .movie_uuid(movieScreeningList.get(i).getMovie_uuid())
                    .date(movieScreeningList.get(i).getDate())
                    .movieHall_uuid(preExistingMovie_halls)
                    .times(preExistingTimes)
                    .build();

            movieScreeningDTOS.add(movieScreeningDTO);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseObject(movieScreeningDTOS)
                        .responseType(ResponseType.SUCCESS)
                        .build(),
                HttpStatus.OK
        );
    }
}
