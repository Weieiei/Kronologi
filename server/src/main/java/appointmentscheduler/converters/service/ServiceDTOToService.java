package appointmentscheduler.converters.service;

import appointmentscheduler.dto.service.ServiceDTO;
import appointmentscheduler.entity.service.ServiceEntity;
import appointmentscheduler.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceDTOToService implements Converter<ServiceDTO, ServiceEntity> {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public ServiceEntity convert(ServiceDTO serviceDTO) {
        ServiceEntity service = new ServiceEntity();

        service.setId(serviceDTO.getId());
        service.setName(serviceDTO.getName());
        service.setDuration(serviceDTO.getDuration());

        return service;
    }
}
