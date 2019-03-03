package appointmentscheduler.repository;

import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndEmailIgnoreCase(long id, String email);
    Optional<User> findByEmailIgnoreCase(String email);
    List<User> findByRoles_Role(RoleEnum role);
    Optional<User> findByIdAndRoles_Role(long id, RoleEnum role);
    User findByIdAndBusinessId(long id, long businessId);
  }
