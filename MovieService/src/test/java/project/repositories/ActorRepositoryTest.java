package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.MovieApplication;
import project.entities.Actor;
import project.entities.Gender;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = MovieApplication.class)
@DirtiesContext
public class ActorRepositoryTest {
    @Autowired
    private ActorRepository actorRepository;

    private Actor actor;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        actor = Actor.builder()
                .gender(Gender.OTHER)
                .name("")
                .uuid(UUID)
                .build();
    }

    @Test
    public void findActorByUuid_returnsFound() {
        actorRepository.save(actor);

        Actor actorFound = actorRepository.findByUuid(UUID).orElse(null);

        assertNotNull(actorFound);
        assertEquals(actorFound.getUuid(), UUID);
    }

    @Test
    public void findActorByUuid_returnsNotFound() {
        Actor actorFound = actorRepository.findByUuid(UUID).orElse(null);

        assertNull(actorFound);
    }

    @Test
    public void deleteActorByUuid_returnsDeleted() {
        actorRepository.save(actor);

        Actor actorFound = actorRepository.findByUuid(UUID).orElse(null);

        assertNotNull(actorFound);

        actorRepository.deleteByUuid(UUID);

        actorFound = actorRepository.findByUuid(UUID).orElse(null);

        assertNull(actorFound);
    }
}
