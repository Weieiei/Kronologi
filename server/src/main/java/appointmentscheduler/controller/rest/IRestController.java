package appointmentscheduler.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class IRestController<T, DTO> {

    @Autowired
    private HttpServletRequest request;

    abstract List<T> findAll();
    abstract T findById(long id);
    abstract T add(DTO dto);
    abstract T update(long id, DTO dto);
    abstract ResponseEntity delete(long id);

    // The user id is taken from the JWT and set as a request attribute, for easy access
    protected Long getUserId() {
        return Long.parseLong(String.valueOf(request.getAttribute("userId")));
    }

}
