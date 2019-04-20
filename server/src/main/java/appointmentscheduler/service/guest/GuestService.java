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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

    public Map<String, Object> register(GuestDTO guestDTO) throws IOException, MessagingException, NoSuchAlgorithmException {


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
        guestVerificationRepository.save(guestVerification);

        String token = generateToken(savedGuest);
        return buildTokenRegisterMap(token, guestVerification);//, verification);
    }

    public PhoneNumber getPhoneNumber(long guestId){
        return phoneNumberRepository.findByGuestId(guestId).orElse(null);
    }

    private String generateToken(Guest guest) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(RoleEnum.GUEST.toString()));
        UserDetails userDetails = userDetailsEmailService.loadUserByUsername(String.valueOf(guest.getId()));
        Authentication authentication = new AnonymousAuthenticationToken(String.valueOf(guest.getId()), userDetails, authorities );
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(guest.getId(), "", authorities));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateGuestToken(guest, authentication);
    }

    private Map<String, Object> buildTokenRegisterMap(String token, Verification verification) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("verification", verification);

        return map;
    }

    private Map<String, Object> buildTokenRegisterMap(String token, GuestVerification guestVerification) {
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("verification", guestVerification);

        return map;
    }


}
