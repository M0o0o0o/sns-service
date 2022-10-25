package mooyeol.snsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SnsServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SnsServiceApplication.class, args);
	}
}
