package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.MovieHall;

import java.util.Optional;

@Repository
public interface MovieHallRepository extends JpaRepository<MovieHall, Integer> {
    Optional<MovieHall> findByUuid(int id);
}
