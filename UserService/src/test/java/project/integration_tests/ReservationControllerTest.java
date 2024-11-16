package project.integration_tests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import project.UserApplication;

@SpringBootTest(classes = UserApplication.class)
@Profile("test")
public class ReservationControllerTest {
    @Test
	void contextLoads() {
	}
}
