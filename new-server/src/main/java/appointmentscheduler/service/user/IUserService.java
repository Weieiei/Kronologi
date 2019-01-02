package appointmentscheduler.service.user;

import appointmentscheduler.dto.Token;
import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import org.springframework.http.ResponseEntity;

public interface IUserService {

    Token register(UserRegisterDTO userRegisterDTO);

    Token login(UserLoginDTO userLoginDTO);

}
