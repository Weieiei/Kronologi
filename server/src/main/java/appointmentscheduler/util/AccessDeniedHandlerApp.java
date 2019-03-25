package appointmentscheduler.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.persistence.Access;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AccessDeniedHandlerApp extends AccessDeniedHandlerImpl {



    private final static Log logger = LogFactory.getLog(AccessDeniedHandlerApp.class);

    private static final String LOG_TEMPLATE = "AccessDeniedHandlerApp:  User attempted to access a resource for which they do not have permission.  User %s attempted to access %s";

    @Override
    public void handle(HttpServletRequest _request, HttpServletResponse _response, AccessDeniedException _exception) throws IOException, ServletException {
        setErrorPage("/securityAccessDenied");  // this is a standard Spring MVC Controller

        super.handle(_request, _response, _exception);
    }


}