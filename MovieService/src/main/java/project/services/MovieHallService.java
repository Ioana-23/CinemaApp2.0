package project.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.MovieHall;
import project.repositories.MovieHallRepository;

@Service
@RequiredArgsConstructor
public class MovieHallService {

    private final MovieHallRepository movieHallRepository;

    public MovieHall findMovieHallByUuid(int uuid)
    {
        return movieHallRepository.findMovieHallByUuid(uuid).orElse(null);
    }
}
