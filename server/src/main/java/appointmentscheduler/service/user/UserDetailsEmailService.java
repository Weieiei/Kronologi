package appointmentscheduler.service.user;

import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Need this because we're loading our user with their id, not username.
 * So we need to override the loadUserByUsername method and provide the id as the parameter instead.
 */
@Service
public class UserDetailsEmailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), user.getPassword(), getAuthorities(user));

    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

//        user.getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
//        });

        authorities.add(new SimpleGrantedAuthority(user.getRole().getRole().name()));

        return authorities;

    }

}
