package appointmentscheduler.util;

import appointmentscheduler.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider implements Serializable {

    private final String KEY = generateRandomSecret();

    // min * sec * ms == 1 hour
    private final int EXPIRATION = 60 * 60 * 1000 * 24;

    public String generateToken(User user, Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .claim("sub", user.getId())
                .claim("roles", authorities)
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .signWith(SignatureAlgorithm.HS256, KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .compact();

    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token, UserDetails userDetails) {

        JwtParser jwtParser = Jwts.parser().setSigningKey(KEY);
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
    }

    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateOfToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public boolean tokenIsExpired(String token) {
        Date expiration = getExpirationDateOfToken(token);
        return expiration.before(new Date());
    }

    public boolean tokenIsValid(String token, UserDetails userDetails) {
        String userId = getUserIdFromToken(token);
        // userDetails.getUsername() is actually the id
        return userId.equals(userDetails.getUsername()) && !tokenIsExpired(token);
    }

    private static String generateRandomSecret() {
        RandomStringGenerator.Builder builder = new RandomStringGenerator.Builder();
        char[] numbers = new char[]{'0', '9'};
        char[] lowercase = new char[]{'a', 'z'};
        char[] uppercase = new char[]{'A', 'Z'};
        builder.withinRange(numbers, lowercase, uppercase);
        RandomStringGenerator generator = builder.build();

        return generator.generate(128);
    }

}
