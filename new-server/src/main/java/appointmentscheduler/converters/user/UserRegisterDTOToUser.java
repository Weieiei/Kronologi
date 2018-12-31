package appointmentscheduler.converters.user;

import appointmentscheduler.dto.user.UserRegisterDTO;
import appointmentscheduler.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterDTOToUser implements Converter<UserRegisterDTO, User> {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User convert(UserRegisterDTO userRegisterDTO) {

        User user = new User();

        user.setFirstName(userRegisterDTO.getFirstName());
        user.setLastName(userRegisterDTO.getLastName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));

        return user;

    }

}
