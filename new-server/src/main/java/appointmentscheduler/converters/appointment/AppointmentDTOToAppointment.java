package appointmentscheduler.converters.appointment;

import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.ServiceRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppointmentDTOToAppointment implements Converter<AppointmentDTO, Appointment> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Appointment convert(AppointmentDTO appointmentDTO) {

        Appointment appointment = new Appointment();

        User client = userRepository.findById(appointmentDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Client with id %d not found.", appointmentDTO.getClientId())));

        User employee = userRepository.findByIdAndRoles_Role(appointmentDTO.getEmployeeId(), RoleEnum.EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.", appointmentDTO.getEmployeeId())));

        Service service = serviceRepository.findById(appointmentDTO.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Service with id %d not found.", appointmentDTO.getServiceId())));

        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setService(service);

        appointment.setStartTime(LocalDateTime.of(
                appointmentDTO.getYear(), appointmentDTO.getMonth(), appointmentDTO.getDay(),
                appointmentDTO.getHour(), appointmentDTO.getMinute()
        ));

        appointment.setNotes(appointmentDTO.getNotes());

        return appointment;

    }
}
