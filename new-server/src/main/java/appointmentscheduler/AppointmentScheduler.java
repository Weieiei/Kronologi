package appointmentscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppointmentScheduler {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentScheduler.class, args);
    }
}
