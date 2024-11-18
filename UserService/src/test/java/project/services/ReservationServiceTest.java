package project.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.Reservation;
import project.entities.Ticket;
import project.entities.User;
import project.entities.UserRole;
import project.repositories.ReservationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {
    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationRepository reservationRepository;
    private static final int UUID = 0;
    private static final String EMAIL = "baciuioana23@gmail.com";
    private static User user;
    private static Reservation reservation;

    @BeforeAll
    public static void initializeEntities() {
        user = User.builder()
                .user_role(UserRole.ADMIN)
                .email(EMAIL)
                .first_name("Ioana")
                .last_name("Baciu")
                .password("pass")
                .uuid(UUID)
                .build();

        reservation = Reservation.builder()
                .user(user)
                .id(1)
                .movie_screening_uuid(1)
                .tickets(List.of(new Ticket()))
                .build();
    }

    @Test
    public void getAllReservationsByUserTest() {
        Mockito.when(reservationRepository.findReservationsByUser(user)).thenReturn(Optional.of(List.of(reservation)));

        List<Reservation> reservationsFound = reservationService.getAllReservationsByUser(user);

        assertNotNull(reservationsFound);
        assertEquals(reservationsFound.getFirst().getId(), 1);
    }

    @Test
    public void getReservationByUuidTest() {
        Mockito.when(reservationRepository.findReservationByUuid(UUID)).thenReturn(Optional.of(reservation));

        Reservation reservationFound = reservationService.getReservationByUuid(UUID);

        assertNotNull(reservationFound);
        assertEquals(reservationFound.getUuid(), UUID);
        assertEquals(reservationFound.getUser().getEmail(), EMAIL);
    }

    @Test
    public void saveReservationTest() {
        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation reservationSaved = reservationService.saveReservation(reservation);

        assertNotNull(reservationSaved);
        assertEquals(reservationSaved.getUuid(), UUID);
        assertEquals(reservationSaved.getUser().getEmail(), EMAIL);
    }
}
