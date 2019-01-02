package appointmentscheduler.service.user;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    ResponseEntity<?> register(UserRegisterDTO userRegisterDTO);

    ResponseEntity<?> login(UserLoginDTO userLoginDTO);

}
