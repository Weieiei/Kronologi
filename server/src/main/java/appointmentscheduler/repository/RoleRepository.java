package appointmentscheduler.repository;

import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleEnum role);
}
