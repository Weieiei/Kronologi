package appointmentscheduler.converters.appointment;

import appointmentscheduler.dto.appointment.CancelAppointmentDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.appointment.CancelledAppointment;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class CancelledDTOToCancelled implements Converter<CancelAppointmentDTO, CancelledAppointment> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository  userRepository;

    @Override
    public CancelledAppointment convert(CancelAppointmentDTO cancelledDTO){

        CancelledAppointment cancelledAppointment = new CancelledAppointment();
        if (appointmentRepository.findById(cancelledDTO.getIdOfCancelledAppointment()).isPresent()) {
            Appointment temp = appointmentRepository.findById(cancelledDTO.getIdOfCancelledAppointment()).get();
            cancelledAppointment.setAppointment(temp);
        }

        if (userRepository.findById(cancelledDTO.getIdPersonWhoCancelled()).isPresent()) {
            User user = userRepository.findById(cancelledDTO.getIdPersonWhoCancelled()).get();
            cancelledAppointment.setCanceller(user);
        }

        cancelledAppointment.setReason(cancelledDTO.getCancelReason());
        return cancelledAppointment;

    }
}
