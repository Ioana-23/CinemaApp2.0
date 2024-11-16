package project;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MovieScreeningApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieScreeningApplication.class, args);
	}
	@Bean
	public ModelMapper movieMapper() {
		return new ModelMapper();
	}
}
