package project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = UserApplication.class)
@ActiveProfiles("test")
public class UserApplicationTest {
    @Test
	void contextLoads() {
	}
}
