package appointmentscheduler.service.guest;

import appointmentscheduler.dto.phonenumber.PhoneNumberDTO;
import appointmentscheduler.dto.user.GuestDTO;
import appointmentscheduler.entity.guest.Guest;
import appointmentscheduler.entity.guest.GuestFactory;
import appointmentscheduler.entity.phonenumber.PhoneNumber;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.verification.GuestVerification;
import appointmentscheduler.entity.verification.Verification;
import appointmentscheduler.repository.GuestRepository;
import appointmentscheduler.repository.GuestVerificationRepository;
import appointmentscheduler.repository.PhoneNumberRepository;
import appointmentscheduler.service.user.UserDetailsEmailService;
import appointmentscheduler.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Logger;

@Service
public class GuestService {
    private static final Logger logger = Logger.getLogger(GuestService.class.getName());
    private final GuestRepository guestRepository;
    private final JwtProvider jwtProvider;
    private final GuestVerificationRepository guestVerificationRepository;
    private final AuthenticationManager authenticationManager;
    private final PhoneNumberRepository phoneNumberRepository;
    private final UserDetailsEmailService userDetailsEmailService;

    @Autowired
    public GuestService(
            GuestRepository guestRepository, JwtProvider jwtProvider,
            GuestVerificationRepository guestVerificationRepository, AuthenticationManager authenticationManager,
            PhoneNumberRepository phoneNumberRepository, UserDetailsEmailService userDetailsEmailService
    ) {
        this.guestRepository = guestRepository;
        this.guestVerificationRepository = guestVerificationRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.phoneNumberRepository = phoneNumberRepository;
        this.userDetailsEmailService = userDetailsEmailService;
    }

    public Guest findGuest(GuestDTO guestDTO) {
        Optional<Guest> guestOptional = guestRepository.findGuestByEmailIgnoreCase(guestDTO.getEmail());
        return guestOptional.orElse(null);
    }

    public Guest register(GuestDTO guestDTO) throws IOException, MessagingException, NoSuchAlgorithmException {

        Guest guest = GuestFactory.createGuest(Guest.class, guestDTO.getFirstName(), guestDTO.getLastName(), guestDTO.getEmail());

        if (guestDTO.getPhoneNumber() != null) {

            PhoneNumberDTO phoneNumberDTO = guestDTO.getPhoneNumber();
            PhoneNumber phoneNumber = new PhoneNumber(
                    phoneNumberDTO.getCountryCode(),
                    phoneNumberDTO.getAreaCode(),
                    phoneNumberDTO.getNumber()
            );

            guest.setPhoneNumber(phoneNumber);
            phoneNumber.setGuest(guest);

        }

        guest.setRole(RoleEnum.GUEST.toString());

        Guest savedGuest = guestRepository.save(guest);

        GuestVerification guestVerification = new GuestVerification(savedGuest);
        return guestVerificationRepository.save(guestVerification).getGuest();

    }

    public PhoneNumber getPhoneNumber(long guestId){
        return phoneNumberRepository.findByGuestId(guestId).orElse(null);
    }

}
