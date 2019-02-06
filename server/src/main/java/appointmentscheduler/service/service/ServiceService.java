package appointmentscheduler.service.service;

import appointmentscheduler.entity.service.Service;
import appointmentscheduler.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import appointmentscheduler.exception.ResourceNotFoundException;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service findById(long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Service with id %d not found.", id)));
    }

    public Service add(Service service) {
        return serviceRepository.save(service);
    }
}
