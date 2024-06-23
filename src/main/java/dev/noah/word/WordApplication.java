package dev.noah.word;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WordApplication {

    public static void main(String[] args) {
        SpringApplication.run(WordApplication.class, args);
    }
}
