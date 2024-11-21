package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.MovieScreeningApplication;
import project.entities.Seat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = MovieScreeningApplication.class)
@DirtiesContext
public class SeatRepositoryTest {
    @Autowired
    private SeatRepository seatRepository;

    private Seat seat;
    private static final int UUID = 0;

    @BeforeEach
    public void init() {
        seat = Seat.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void findSeatByUuid_returnsFound() {
        seatRepository.save(seat);
        Seat seatFound = seatRepository.findSeatByUuid(UUID).orElse(null);
        assertNotNull(seatFound);
        assertEquals(seatFound.getUuid(), UUID);
    }

    @Test
    public void findSeatByUuid_returnsUserNotFound() {
        Seat seatFound = seatRepository.findSeatByUuid(UUID).orElse(null);
        assertNull(seatFound);
    }

}
