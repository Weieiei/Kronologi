package appointmentscheduler.service.service;

import appointmentscheduler.entity.service.Service;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Service findByIdAndBusinessId(long id, long businessId) {
        return serviceRepository.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Service with id %d not found for " +
                        "business with id %d.", id, businessId)));
    }

    public Map<String, String> add(Service service) {
        serviceRepository.save(service);
        return message("Service successfully added.");
    }

    public List<Service> findByBusinessId(long businessId) {
        return serviceRepository.findServicesByBusinessId(businessId);
    }

    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
