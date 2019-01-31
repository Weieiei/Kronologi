package appointmentscheduler.converters.review;

import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.entity.role.RoleEnum;
import appointmentscheduler.entity.service.Service;
import appointmentscheduler.entity.user.User;
import appointmentscheduler.exception.ModelValidationException;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ReviewAlreadyExistsException;
import appointmentscheduler.repository.AppointmentRepository;
import appointmentscheduler.repository.ReviewRepository;
import appointmentscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReviewDTOToReview implements Converter<ReviewDTO, Review> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review convert(ReviewDTO reviewDTO) {
        Review review = new Review();
        //verify that there is an appointment existing with the given appointment id
        if (appointmentRepository.findById(reviewDTO.getAppointmentId()).isPresent()) {
            Appointment appointment = appointmentRepository.findById(reviewDTO.getAppointmentId()).get();
            //if the review table already has the given appointment id, it means there is already a review for it
            if (reviewRepository.findByAppointmentId(appointment.getId())!=null){
                throw new ReviewAlreadyExistsException(String.format("Appointment with id %d already has a review.",
                        reviewDTO.getAppointmentId()));
            }
            //if there is no review for the given appointment id, then add the appointment to the review
            else{
                review.setAppointment(appointment);
            }
        //there is no appointment that exists with the current appointment id
        } else {
            throw new ResourceNotFoundException(String.format("Appointment with id %d not found.",
                    reviewDTO.getAppointmentId()));
        }
        User employee = userRepository.findByIdAndRoles_Role(reviewDTO.getEmployeeId(), RoleEnum.EMPLOYEE)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %d not found.",
                        reviewDTO.getEmployeeId())));
        User client = userRepository.findByIdAndRoles_Role(reviewDTO.getClientId(), RoleEnum.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Client with id %d not found.",
                        reviewDTO.getClientId())));

        Service service =
                employee.getEmployeeServices().stream().filter(s -> s.getId() == reviewDTO.getServiceId()).findFirst()
                        .orElseThrow(() -> new ModelValidationException(String.format("%s doesn't provided the desired service.", employee.getFullName())));

        review.setContent(reviewDTO.getContent());
        review.setCustomer(client);
        review.setEmployee(employee);
        review.setService(service);
        return review;
    }

}
