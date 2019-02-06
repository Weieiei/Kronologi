package appointmentscheduler.converters.appointment;

import appointmentscheduler.dto.appointment.AppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ModelValidationException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.ShiftRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class AppointmentDTOToAppointment implements Converter<AppointmentDTO, Appointment> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Appointment convert(AppointmentDTO appointmentDTO) {

        Appointment appointment = new Appointment();

        if (appointmentDTO.getClientId() == appointmentDTO.getEmployeeId()) {
            throw new ModelValidationException("You cannot book an appointment with yourself.");
        }

        // Check if you're a valid client
        User client = userRepository.findByIdAndRoles_Role(appointmentDTO.getClientId(), RoleEnum.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Client with id %d not found.", appointmentDTO.getClientId())));

        // Check if the provided employee ID belongs to a user with the employee role
        User employee = userRepository.findByIdAndRoles_Role(appointmentDTO.getEmployeeId(), RoleEnum.EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.", appointmentDTO.getEmployeeId())));

        // Check if this employee provides the desired service
        Service service = employee.getEmployeeServices().stream().filter(s -> s.getId() == appointmentDTO.getServiceId()).findFirst()
                .orElseThrow(() -> new ModelValidationException(String.format("%s doesn't provided the desired service.", employee.getFullName())));

        appointment.setClient(client);
        appointment.setEmployee(employee);
        appointment.setService(service);

        appointment.setDate(LocalDate.of(appointmentDTO.getYear(), appointmentDTO.getMonth(), appointmentDTO.getDay()));
        appointment.setStartTime(LocalTime.of(appointmentDTO.getHour(), appointmentDTO.getMinute()));
        appointment.setEndTime(appointment.getStartTime().plusMinutes(service.getDuration()));

        // Check if appointment fits in one of employee's shifts
        shiftRepository.findByEmployeeId(employee.getId()).stream().filter(shift ->
                shift.getStartDateTime().isBefore(appointment.getStartDateTime().plusSeconds(1)) && shift.getEndDateTime().isAfter(appointment.getEndDateTime().minusSeconds(1))
        ).findFirst().orElseThrow(() -> new ModelValidationException(String.format("This appointment does not fit in %s's schedule.", employee.getFullName())));

        // Check if it conflicts with another appointment
        Appointment conflictAppointment = appointmentRepository.findByEmployeeId(employee.getId()).stream().filter(a ->
                !((a.getStartDateTime().isAfter(appointment.getStartDateTime().minusSeconds(1)) && a.getStartDateTime().isAfter(appointment.getEndDateTime().minusSeconds(1))) ||
                        (a.getEndDateTime().isBefore(appointment.getStartDateTime().plusSeconds(1)) && a.getEndDateTime().isBefore(appointment.getEndDateTime().plusSeconds(1))))
        ).findFirst().orElse(null);

        if (conflictAppointment != null) {
            throw new ModelValidationException(String.format("This appointment conflicts with another in %s's schedule.", employee.getFullName()));
        }

        appointment.setNotes(appointmentDTO.getNotes());

        return appointment;

    }
}
