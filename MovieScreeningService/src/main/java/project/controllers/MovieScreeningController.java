package project.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.controllers.response.Response;
import project.controllers.response.ResponseType;
import project.dtos.MovieDTO;
import project.dtos.MovieScreeningDTO;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.services.MovieHallService;
import project.services.MovieScreeningService;

import java.io.IOException;

@RestController
@RequestMapping("project/movie_screenings")
@RequiredArgsConstructor
public class MovieScreeningController {
    private final MovieScreeningService movieScreeningService;
    private final MovieHallService movieHallService;
    private final ModelMapper modelMapper;
    private final MovieControllerProxy movieControllerProxy;

    @PostMapping("/movie_screening")
    public ResponseEntity<Response> saveMovieScreening(@RequestBody MovieScreeningDTO movieScreeningDTO) {
        MovieScreening movieScreeningToSave = modelMapper.map(movieScreeningDTO, MovieScreening.class);
        try {
            MovieDTO movieFoundDTO = movieControllerProxy.getMovieByUuid(movieScreeningToSave.getMovie_uuid());
            if (movieFoundDTO == null) {
                return new ResponseEntity<>(
                        Response.builder()
                                .message("Movie with id " + movieScreeningToSave.getMovie_uuid() + " doesn't exist")
                                .responseType(ResponseType.ERROR)
                                .build(),
                        HttpStatus.NO_CONTENT);
            }
            MovieHall movieHallFound = movieHallService.getMovieHallByUuid(movieScreeningDTO.getMovieHall_uuid());
            if (movieHallFound == null) {
                return new ResponseEntity<>(
                        Response.builder()
                                .responseType(ResponseType.ERROR)
                                .message("Movie hall with id " + movieScreeningDTO.getMovieHall_uuid() + " doesn't exist")
                                .build(),
                        HttpStatus.NO_CONTENT);
            }
            movieScreeningToSave.setMovieHall(movieHallFound);
            MovieScreening movieScreeningAlreadyExists = movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(movieScreeningToSave.getDate(), movieScreeningToSave.getTime(), movieHallFound);
            if (movieScreeningAlreadyExists == null) {
                return new ResponseEntity<>(
                        Response.builder()
                                .responseObject(movieScreeningService.saveMovieScreening(movieScreeningToSave))
                                .responseType(ResponseType.SUCCESS)
                                .build(),
                        HttpStatus.CREATED);
            }
            return new ResponseEntity<>(
                    Response.builder()
                            .responseType(ResponseType.ERROR)
                            .message("Movie screening on " + movieScreeningToSave.getDate() + " at " + movieScreeningToSave.getTime() + " in movie hall with id " + movieHallFound.getUuid() + " already exists")
                            .build(),
                    HttpStatus.OK);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        MovieScreening movieScreeningFinal = modelMapper.map(movieScreeningDTO, MovieScreening.class);
        MovieHall movieHallToUpdate = movieHallService.getMovieHallByUuid(movieScreeningDTO.getMovieHall_uuid());
        if (movieHallToUpdate == null) {
            return new ResponseEntity<>(
                    Response.builder()
                            .responseType(ResponseType.ERROR)
                            .message("Movie hall with id " + movieScreeningDTO.getMovieHall_uuid() + " doesn't exist")
                            .build(),
                    HttpStatus.NO_CONTENT);
        }
        movieScreeningFinal.setMovieHall(movieHallToUpdate);
        MovieScreening movieScreeningAlreadyExists = movieScreeningService.getMovieScreeningByDateAndTimeAndMovieHall(movieScreeningFinal.getDate(), movieScreeningFinal.getTime(), movieHallToUpdate);
        if (movieScreeningAlreadyExists == null) {
            movieScreeningService.updateMovieScreening(movieScreeningToUpdate, movieScreeningFinal);
            MovieScreening newMovieScreening = movieScreeningService.getMovieScreeningByUuid(movieScreeningFinal.getUuid());
            return new ResponseEntity<>(
                    Response.builder()
                            .responseType(ResponseType.SUCCESS)
                            .responseObject(newMovieScreening)
                            .build(),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(
                Response.builder()
                        .responseType(ResponseType.ERROR)
                        .message("Movie screening on " + movieScreeningFinal.getDate() + " at " + movieScreeningFinal.getTime() + " in movie hall with id " + movieHallToUpdate.getUuid() + " already exists")
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
}
