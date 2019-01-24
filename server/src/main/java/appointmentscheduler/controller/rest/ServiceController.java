package appointmentscheduler.controller.rest;

import appointmentscheduler.entity.service.Service;
import appointmentscheduler.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${rest.api.path}/services")
@PreAuthorize("hasAuthority('CLIENT')")
public class ServiceController extends IRestController {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping
    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public Object findById(long id) {
        return null;
    }

    @Override
    public Object add(Object o) {
        return null;
    }

    @Override
    public Object update(long id, Object o) {
        return null;
    }

    @Override
    public ResponseEntity delete(long id) {
        return null;
    }
}
