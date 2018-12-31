package appointmentscheduler.converters.user;

import appointmentscheduler.dto.user.UserLoginDTO;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserLoginDTOToUser implements Converter<UserLoginDTO, User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String INCORRECT_CREDENTIALS = "Incorrect email and/or password.";

    @Override
    public User convert(UserLoginDTO userLoginDTO) {

        User user = userRepository.findUserByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException(INCORRECT_CREDENTIALS));

        boolean match = bCryptPasswordEncoder.matches(userLoginDTO.getPassword(), user.getPassword());

        if (!match) {
            throw new BadCredentialsException(INCORRECT_CREDENTIALS);
        }

        return user;

    }

}
