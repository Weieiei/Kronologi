package appointmentscheduler.util;

import appointmentscheduler.service.user.UserDetailsEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsEmailService userDetailsEmailService;

    @Autowired
    private JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String header = httpServletRequest.getHeader("Authorization");
        String token = null;
        String userId = null;
        String firstName = null;
        String lastName = null;
        String email = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.split(" ")[1];

            if (!token.equals("null")) {
                try {
                    userId = jwtProvider.getUserIdFromToken(token);
                    firstName = jwtProvider.getFirstNameFromToken(token);
                    lastName = jwtProvider.getLastNameFromToken(token);
                    email = jwtProvider.getEmailFromToken(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            logger.warn("Could not find valid authorization header.");
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsEmailService.loadUserByUsername(userId);

            if (jwtProvider.tokenIsValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getAuthentication(token, userDetails);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            httpServletRequest.setAttribute("userId", userId);
            httpServletRequest.setAttribute("firstName", firstName);
            httpServletRequest.setAttribute("lastName", lastName);
            httpServletRequest.setAttribute("email", email);

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
