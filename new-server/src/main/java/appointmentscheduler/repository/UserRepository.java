package appointmentscheduler.repository;

import appointmentscheduler.entity.User;
import appointmentscheduler.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmail(String email);
    User findUserByUserType(UserType userType);

}
