package sparta.m6nytooneproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class M6nyToOneProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(M6nyToOneProjectApplication.class, args);
    }

}
