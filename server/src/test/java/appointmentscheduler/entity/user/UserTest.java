package appointmentscheduler.entity.user;

import appointmentscheduler.dto.user.UserLoginDTO;
import org.junit.Test;
import org.modelmapper.ModelMapper;

public class UserTest {

    private static final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void checkUserMapping() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("admin@admin.com", "admin123");

        User user = modelMapper.map(userLoginDTO, User.class);
//        return user;
    }

}