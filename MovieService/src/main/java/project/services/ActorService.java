package project.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.entities.Actor;
import project.repositories.ActorRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;

    public Actor getActorByUuid(int id) {
        Optional<Actor> actorFound = actorRepository.findByUuid(id);
        return actorFound.orElse(null);
    }

    @Transactional
    public void removeActorByUuid(int id) {
        actorRepository.deleteByUuid(id);
    }
}
