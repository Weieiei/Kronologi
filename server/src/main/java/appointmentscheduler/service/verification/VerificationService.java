package appointmentscheduler.service.verification;

import appointmentscheduler.entity.role.Role;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.UserRepository;
import appointmentscheduler.repository.VerificationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationService {

    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;

    public VerificationService(UserRepository userRepository, VerificationRepository verificationRepository) {
        this.userRepository = userRepository;
        this.verificationRepository = verificationRepository;
    }

    public boolean verify(String hash)
    {
       Verification verification =  verificationRepository.findByHash(hash);
       if (verification == null) {
           throw new ResourceNotFoundException("Hash not found.");
       }
       Optional<User> userList = userRepository.findById(verification.getUser().getId());
       User currentUser = userList.get();
       currentUser.setVerified(true);
       userRepository.save(currentUser);
       verificationRepository.delete(verification);
       return true;
    }
}
