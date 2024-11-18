package project.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.UserApplication;
import project.entities.User;
import project.entities.UserRole;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = UserApplication.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest()
    {
        User userToSave = User.builder()
                .user_role(UserRole.ADMIN)
                .email("baciuioana23@gmail.com")
                .first_name("Ioana")
                .last_name("Baciu")
                .password("pass")
                .uuid(1)
                .build();

        userRepository.save(userToSave);

        assertTrue(userToSave.getId() > 0);

    }
}
