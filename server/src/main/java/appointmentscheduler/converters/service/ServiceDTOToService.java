package appointmentscheduler.converters.service;

import appointmentscheduler.dto.service.ServiceCreateDTO;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceDTOToService implements Converter<ServiceCreateDTO, Service> {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Service convert(ServiceCreateDTO serviceDTO) {
        Service service = new Service();

       // service.setId(serviceDTO.getId());
        service.setName(serviceDTO.getName());
        service.setDuration(serviceDTO.getDuration());

        return service;
    }
}
