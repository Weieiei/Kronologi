package appointmentscheduler.service.service;

import appointmentscheduler.entity.service.ServiceEntity;
import appointmentscheduler.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import appointmentscheduler.exception.ResourceNotFoundException;

@Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public ServiceEntity findById(long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Service with id %d not found.", id)));
    }

    public ServiceEntity add(ServiceEntity service) {
        return serviceRepository.save(service);
    }
}
