package appointmentscheduler.repository;

import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    List<User> findUserByUserType(UserType userType);

}
