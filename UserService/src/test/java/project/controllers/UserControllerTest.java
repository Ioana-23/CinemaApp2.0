package project.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.UserApplication;
import project.entities.User;
import project.entities.UserRole;
import project.services.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@ContextConfiguration(classes = UserApplication.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static User user;
    private static final int UUID = 0;
    private static final String EMAIL = "baciuioana23@gmail.com";

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
    }

    @Test
    public void getAllUsers_returnsUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/project/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is(EMAIL)));
    }
}
