package appointmentscheduler.service.guest;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.user.GuestDTO;
import appointmentscheduler.entity.guest.Guest;
import appointmentscheduler.entity.guest.GuestFactory;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.repository.*;
import appointmentscheduler.util.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class GuestService {
    private static final Logger logger = Logger.getLogger(GuestService.class.getName());
    private final GuestRepository guestRepository;
    private final JwtProvider jwtProvider;
    private final VerificationRepository verificationRepository;
    private final AuthenticationManager authenticationManager;
    private final PhoneNumberRepository phoneNumberRepository;

    @Autowired
    public GuestService(
            GuestRepository guestRepository, JwtProvider jwtProvider,
            VerificationRepository verificationRepository, AuthenticationManager authenticationManager,
            PhoneNumberRepository phoneNumberRepository
    ) {
        this.guestRepository = guestRepository;
        this.verificationRepository = verificationRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.phoneNumberRepository = phoneNumberRepository;
    }

    public Map<String, Object> register(GuestDTO guestDTO) throws IOException, MessagingException, NoSuchAlgorithmException {


        Guest guest = GuestFactory.createGuest(Guest.class, guestDTO.getFirstName(), guestDTO.getLastName(), guestDTO.getEmail());

//        if (guestDTO.getPhoneNumber() != null) {
//
//            PhoneNumberDTO phoneNumberDTO = guestDTO.getPhoneNumber();
//            PhoneNumber phoneNumber = new PhoneNumber(
//                    phoneNumberDTO.getCountryCode(),
//                    phoneNumberDTO.getAreaCode(),
//                    phoneNumberDTO.getNumber()
//            );
//
//            guest.setPhoneNumber(phoneNumber);
//            phoneNumber.setUser(guest);
//
//        }
        guest.setRole(RoleEnum.GUEST.toString());

        Guest savedGuest = guestRepository.save(guest);

        Verification verification = new Verification(savedGuest);
        verificationRepository.save(verification);

        String token = generateToken(savedGuest);
        return buildTokenRegisterMap(token, verification);//, verification);
    }

    private String generateToken(Guest guest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(guest.getId(), "abc"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateGuestToken(guest, authentication);
    }

    private Map<String, Object> buildTokenRegisterMap(String token, Verification verification) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("verification", verification);

        return map;
    }

//    public Map<String, Object> createGuest (UserRegisterDTO guestDTO){
//        if (guestRepository.findByEmailIgnoreCase(guestDTO.getEmail()).orElse(null) != null) {
//            throw new UserAlreadyExistsException(String.format("A guest with the email %s already exists.", guestDTO.getEmail()));
//        }
//
//        User guest = UserFactory.createGuest(User.class, guestDTO.getFirstName(), guestDTO.getLastName(), guestDTO.getEmail());
//
//        guest.setRole(RoleEnum.GUEST.toString());
//        User savedUser = guestRepository.save(guest);
//        Verification verification = new Verification(savedUser);
//
//        verificationRepository.save(verification);
//
//        String token = generateToken(savedUser, guestDTO.getPassword());
//
//        return guestRepository( token, verification);
//
//    }
}
