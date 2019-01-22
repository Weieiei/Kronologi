package appointmentscheduler.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {

    @Autowired
    private HttpServletRequest request;

    protected Long getUserId() {
        try {
            return Long.parseLong(String.valueOf(request.getAttribute("userId")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String getUserEmail() {
        return String.valueOf(request.getAttribute("email"));
    }

}
