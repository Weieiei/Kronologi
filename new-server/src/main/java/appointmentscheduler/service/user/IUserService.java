package appointmentscheduler.service.user;

import appointmentscheduler.entity.user.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    ResponseEntity<String> register(User user);

    ResponseEntity<String> login(User user);

}
