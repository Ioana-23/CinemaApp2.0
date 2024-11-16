package project.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import project.entities.Reservation;
import project.entities.Ticket;
import project.entities.User;
import project.entities.UserRole;
import project.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationRepository reservationRepository;
    private static final int UUID = 0;
    private static final String EMAIL = "baciuioana23@gmail.com";
    @Test
    public void testGetAllReservationsByUser()
    {
        User userToFind = User.builder()
                .user_role(UserRole.ADMIN)
                .email(EMAIL)
                .first_name("Ioana")
                .last_name("Baciu")
                .password("pass")
                .uuid(UUID)
                .build();

        Reservation reservationToFind = Reservation.builder()
                .user(userToFind)
                .id(1)
                .movie_screening_uuid(1)
                .tickets(List.of(new Ticket()))
                .build();

        Mockito.when(reservationRepository.findReservationsByUser(userToFind)).thenReturn(Optional.of(List.of(reservationToFind)));

        List<Reservation> reservationsFound = reservationService.getAllReservationsByUser(userToFind);

        assertNotNull(reservationsFound);
        assertEquals(reservationsFound.getFirst().getId(), 1);
    }
}
