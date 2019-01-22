package appointmentscheduler.service.verification;

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
       Verification verif =  verificationRepository.findByHash(hash);
       if (verif == null) {
           throw new ResourceNotFoundException("Hash not found.");
       }
       System.out.println("Hash: " + hash);
       Optional<User> userList = userRepository.findById(verif.getUser().getId());
       User currentUser = userList.get();
       System.out.println("Email: " + currentUser.getEmail());
       currentUser.setVerified(true);
       userRepository.save(currentUser);
       return true;
    }
}
