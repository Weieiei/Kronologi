package appointmentscheduler.util;

import appointmentscheduler.service.user.UserDetailsEmailService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class GoogleApiFilter extends OncePerRequestFilter {

    String userId = null;


    @Autowired
    private UserDetailsEmailService userDetailsEmailService;

    @Autowired
    private JwtProvider jwtProvider;

    private final static Log logger = LogFactory.getLog(GoogleApiFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            // do nothing
        } else {
            Map<String,String[]> params = httpServletRequest.getParameterMap();
            if (!params.isEmpty() && params.containsKey("state")) {
                String token = params.get("state")[0];
                if (token != null) {

                    userId = jwtProvider.getUserIdFromGoogleToken(token);
//                    UserDetails userDetails = userDetailsEmailService.loadUserByUsername(userId);
//
//                    UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getGoogleAuthentication(token, userDetails);
//                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                    httpServletRequest.setAttribute("userId", userId);
                }
            }
        }

        httpServletRequest.getSession().setAttribute("userId",userId);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}

