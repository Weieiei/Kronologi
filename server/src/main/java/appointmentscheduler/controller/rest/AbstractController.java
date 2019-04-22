package appointmentscheduler.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractController {

    @Autowired
    private ServletRequest request1;
    @Autowired
    private HttpServletRequest request;

    protected Long getUserId() {
        try {
            return Long.parseLong(String.valueOf(request.getAttribute("userId")));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String getIpAddresss(){
       return request.getRemoteAddr();
    }

    protected String getSessionUser() { return String.valueOf(request.getSession().getAttribute("userId"));}
    protected String getUserEmail() {
        return String.valueOf(request.getAttribute("email"));
    }

    protected ResponseEntity<String> getJson(ObjectMapper mapper, Object object) {
        try {
            return ResponseEntity.ok(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
