package appointmentscheduler.controller.rest;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRestController<T, DTO> {
    List<T> findAll();
    T findById(long id);
    T add(DTO dto);
    T update(long id, DTO dto);
    ResponseEntity delete(long id);
}
