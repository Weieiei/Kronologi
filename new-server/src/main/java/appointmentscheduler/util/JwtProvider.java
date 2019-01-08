package appointmentscheduler.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    private final String KEY = "JuFXDen8amvNPnTE4zwJ7jE2U7VufugLhEfJK8McxhHKcVd4dmJZu9GwPRuTcCTb2MxN68htue9WuPV5HL6H3nr6p4tdQuTg7vm2";
    private final String ROLES = "roles";

    // min * sec * ms == 1 hour
    private final int EXPIRATION = 60 * 60 * 1000 * 24;

    public String generateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                // putting the user id as the subject
                .setSubject(authentication.getName())
                .claim(ROLES, authorities)
                .signWith(SignatureAlgorithm.HS256, KEY)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .compact();

    }

    // TODO remove authentication from params
    public UsernamePasswordAuthenticationToken getAuthentication(String token, Authentication authentication, UserDetails userDetails) {

        JwtParser jwtParser = Jwts.parser().setSigningKey(KEY);
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        // TODO stream.of
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(ROLES).toString().split(","))
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

}
