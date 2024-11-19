package project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import project.entities.User;
import project.entities.UserRole;
import project.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private static final int UUID = 0;
    private static final String EMAIL = "baciuioana23@gmail.com";
    private User user;

    @BeforeEach
    public void init() {
        user = User.builder()
                .user_role(UserRole.ADMIN)
                .email(EMAIL)
                .first_name("Ioana")
                .last_name("Baciu")
                .password("pass")
                .uuid(UUID)
                .build();
    }

    @Test
    public void getUserById_returnsUser() {
        Mockito.when(userRepository.findUserByUuid(UUID)).thenReturn(Optional.of(user));

        User userFound = userService.getUserByUuid(UUID);

        assertNotNull(userFound);
        assertEquals(userFound.getUuid(), UUID);
        assertEquals(userFound.getEmail(), EMAIL);
    }

    @Test
    public void getAllUsers_returnsUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertEquals(users.size(), 1);
        assertEquals(users.getFirst().getUuid(), UUID);
    }
}
