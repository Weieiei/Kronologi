package appointmentscheduler;

import appointmentscheduler.property.TwilioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppointmentScheduler {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(TwilioProperties.class);
        SpringApplication.run(AppointmentScheduler.class, args);
    }
}
