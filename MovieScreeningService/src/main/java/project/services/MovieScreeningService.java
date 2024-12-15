package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.MovieHall;
import project.entities.MovieScreening;
import project.repositories.MovieScreeningRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieScreeningService {
    private final MovieScreeningRepository movieScreeningRepository;

    public MovieScreening saveMovieScreening(MovieScreening movieScreening) {
        return movieScreeningRepository.save(movieScreening);
    }

    public MovieScreening getMovieScreeningByUuid(int uuid) {
        return movieScreeningRepository.findMovieScreeningByUuid(uuid).orElse(null);
    }

    public MovieScreening getMovieScreeningByDateAndTimeAndMovieHall(LocalDate date, LocalTime time, MovieHall movieHall)
    {
        Optional<MovieScreening> movieScreening = movieScreeningRepository.findMovieScreeningByDateAndTimeAndMovieHall(date, time, movieHall);
        return movieScreening.orElse(null);
    }

    @Transactional
    public void updateMovieScreening(MovieScreening movieScreeningToUpdate, MovieScreening movieScreeningFinal) {
        movieScreeningToUpdate.setMovieHall(movieScreeningFinal.getMovieHall());
        movieScreeningToUpdate.setDate(movieScreeningFinal.getDate());
        movieScreeningToUpdate.setTime(movieScreeningFinal.getTime());
        movieScreeningRepository.save(movieScreeningToUpdate);
    }

    public List<MovieScreening> getMovieScreeningsByDate(LocalDate date)
    {
        return movieScreeningRepository.findMovieScreeningsByDate(date).orElse(null);
    }
}
