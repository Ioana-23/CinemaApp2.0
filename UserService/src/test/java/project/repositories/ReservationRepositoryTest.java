package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.UserApplication;
import project.entities.Reservation;
import project.entities.User;
import project.entities.UserRole;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = UserApplication.class)
@DirtiesContext
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    private User user;
    private static final int UUID = 0;
    private Reservation reservation;

    @BeforeEach
    public void init() {
        user = User.builder()
                .user_role(UserRole.ADMIN)
                .email("baciuioana23@gmail.com")
                .first_name("Ioana")
                .last_name("Baciu")
                .password("pass")
                .uuid(UUID)
                .build();

        userRepository.save(user);

        reservation = Reservation.builder()
                .user(user)
                .uuid(UUID)
                .movie_screening_uuid(UUID)
                .build();
    }

    @Test
    public void findReservationById_returnsFound() {
        reservationRepository.save(reservation);
        Reservation reservationFound = reservationRepository.findReservationByUuid(UUID).orElse(null);
        assertNotNull(reservationFound);
        assertEquals(reservationFound.getUuid(), UUID);
    }

    @Test
    public void findReservationById_returnsNotFound() {
        Reservation reservationFound = reservationRepository.findReservationByUuid(UUID).orElse(null);
        assertNull(reservationFound);
    }

    @Test
    public void findReservationsByUser_returnsFound() {
        reservationRepository.save(reservation);
        List<Reservation> reservationsFound = reservationRepository.findReservationsByUser(user).orElse(null);
        assertNotNull(reservationsFound);
        assertEquals(reservationsFound.size(), 1);
        assertEquals(reservationsFound.getFirst().getUuid(), UUID);
    }

    @Test
    public void findReservationsByUser_returnsNone() {
        List<Reservation> reservationsFound = reservationRepository.findReservationsByUser(user).orElse(null);
        assertNotNull(reservationsFound);
        assertEquals(reservationsFound.size(), 0);
    }

    @Test
    public void deleteReservatation_returnsDeleted() {
        reservationRepository.save(reservation);
        Reservation reservationFound = reservationRepository.findReservationByUuid(UUID).orElse(null);
        assertNotNull(reservationFound);
        assertEquals(reservationFound.getUuid(), UUID);

        reservationRepository.deleteByUuid(UUID);

        reservationFound = reservationRepository.findReservationByUuid(UUID).orElse(null);
        assertNull(reservationFound);
    }

}
