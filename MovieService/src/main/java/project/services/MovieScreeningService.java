package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.MovieScreening;
import project.repositories.MovieScreeningRepository;

@Service
@RequiredArgsConstructor
public class MovieScreeningService {
    private final MovieScreeningRepository movieScreeningRepository;

    public MovieScreening saveMovieScreening(MovieScreening movieScreening) {
        return movieScreeningRepository.save(movieScreening);
    }

    public MovieScreening findMovieScreeningByUuid(int uuid)
    {
        return movieScreeningRepository.findMovieScreeningByUuid(uuid).orElse(null);
    }

    @Transactional
    public void updateMovieScreening(MovieScreening movieScreeningToUpdate, MovieScreening movieScreeningFinal)
    {
//        movieScreeningToUpdate.setMovieHall(movieScreeningFinal.getMovieHall());
        movieScreeningToUpdate.setDate(movieScreeningFinal.getDate());
        movieScreeningToUpdate.setTime(movieScreeningFinal.getTime());
        movieScreeningRepository.save(movieScreeningToUpdate);
    }
}
