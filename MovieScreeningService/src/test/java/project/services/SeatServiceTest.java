package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.Seat;
import project.repositories.SeatRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {
    @InjectMocks
    private SeatService seatService;
    @Mock
    private SeatRepository seatRepository;
    private static final int UUID = 0;
    private Seat seat;

    @BeforeEach
    public void init() {
        seat = Seat.builder()
                .uuid(UUID)
                .build();
    }

    @Test
    public void getSeatByUuid_returnsSeat() {
        Mockito.when(seatRepository.findSeatByUuid(UUID)).thenReturn(Optional.of(seat));

        Seat seatFound = seatService.getSeatByUuid(UUID);

        assertNotNull(seatFound);
        assertEquals(seatFound.getUuid(), UUID);
    }
}
