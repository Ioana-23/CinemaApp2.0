package project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.MovieHall;
import project.repositories.MovieHallRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieHallService {

    private final MovieHallRepository movieHallRepository;

    public MovieHall getMovieHallByUuid(int uuid) {
        return movieHallRepository.findMovieHallByUuid(uuid).orElse(null);
    }

    public List<MovieHall> getAllMovieHalls() {
        return movieHallRepository.findAll();
    }
}
