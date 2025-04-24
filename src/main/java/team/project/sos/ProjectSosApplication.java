package team.project.sos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ProjectSosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectSosApplication.class, args);
    }

}
