package project.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import project.UserApplication;
import project.entities.User;
import project.entities.UserRole;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = UserApplication.class)
@DirtiesContext
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;
    private static final int UUID = 0;

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
    }

    @Test
    public void findUserById_returnsFound() {
        userRepository.save(user);
        User userFound = userRepository.findUserByUuid(UUID).orElse(null);
        assertNotNull(userFound);
        assertEquals(userFound.getUuid(), UUID);
    }

    @Test
    public void findUserById_returnsUserNotFound() {
        User userFound = userRepository.findUserByUuid(UUID).orElse(null);
        assertNull(userFound);
    }

}
