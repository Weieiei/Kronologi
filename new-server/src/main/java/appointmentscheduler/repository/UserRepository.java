package appointmentscheduler.repository;

import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.user.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByIdAndUserType(long id, UserType type);
    List<User> findUserByUserType(UserType userType);

}
