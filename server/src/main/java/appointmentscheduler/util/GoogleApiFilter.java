package appointmentscheduler.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class GoogleApiFilter implements Filter {

    private final static Log logger = LogFactory.getLog(GoogleApiFilter.class);

    @Override
    public void init(FilterConfig fc) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
            // do nothing
        } else {
            Map<String,String[]> params = req.getParameterMap();
            if (!params.isEmpty() && params.containsKey("code")) {
                String token = params.get("code")[0];
                if (token != null) {
                    Authentication auth = new TokenAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        fc.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }

    class TokenAuthentication implements Authentication {
        private String token;
        private TokenAuthentication(String token) {
            this.token = token;
        }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return new ArrayList<GrantedAuthority>(0);
        }
        @Override
        public Object getCredentials() {
            return token;
        }
        @Override
        public Object getDetails() {
            return null;
        }
        @Override
        public Object getPrincipal() {
            return null;
        }
        @Override
        public boolean isAuthenticated() {
            return false;
        }
        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        }
        @Override
        public String getName() {
            return "hel";
        }
    }

}

