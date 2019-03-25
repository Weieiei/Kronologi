package appointmentscheduler.converters.appointment;

import appointmentscheduler.dto.appointment.CancelAppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.business.Business;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.BusinessRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class CancelledDTOToCancelled implements Converter<CancelAppointmentDTO, CancelledAppointment> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;


    @Override
    public CancelledAppointment convert(CancelAppointmentDTO cancelledDTO) {

        CancelledAppointment cancelledAppointment = new CancelledAppointment();
        Business business = businessRepository.findById(cancelledDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Business with id %d not found.",
                        cancelledDTO.getBusinessId())));
        cancelledAppointment.setBusiness(business);
        Appointment appointment = appointmentRepository.findByIdAndBusinessId(cancelledDTO.getIdOfCancelledAppointment(), cancelledDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Appointment with id %d and " +
                                "business with id %d not found.",
                        cancelledDTO.getIdOfCancelledAppointment(), cancelledDTO.getBusinessId())));
        cancelledAppointment.setAppointment(appointment);
        User user = userRepository.findById(cancelledDTO.getIdPersonWhoCancelled())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %d not found.",
                        cancelledDTO.getIdPersonWhoCancelled())));
        cancelledAppointment.setCanceller(user);
        cancelledAppointment.setReason(cancelledDTO.getCancelReason());
        return cancelledAppointment;

    }
}
