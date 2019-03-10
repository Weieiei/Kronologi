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
//    Optional<User> findByIdAndRoles_Role(long id, RoleEnum role);
Optional<User> findByIdAndBusinessId(long id, long businessId);

    Optional<List<User>> findAllByBusinessId(long businessId);

    List<User> findByRole(RoleEnum role);
  }
