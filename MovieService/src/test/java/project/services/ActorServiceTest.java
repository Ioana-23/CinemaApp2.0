package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.Actor;
import project.repositories.ActorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {
    @InjectMocks
    private ActorService actorService;
    @Mock
    private ActorRepository actorRepository;
    private static final int UUID = 0;
    private Actor actor;

    @BeforeEach
    public void init() {
        actor = Actor.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void getActorByUuid_returnsFound() {
        Mockito.when(actorRepository.findByUuid(UUID)).thenReturn(Optional.of(actor));

        Actor actorFound = actorService.getActorByUuid(UUID);

        assertNotNull(actorFound);
        assertEquals(actorFound.getUuid(), UUID);
    }
}
