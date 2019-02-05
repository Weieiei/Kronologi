package appointmentscheduler.converters.review;

import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.appointment.Appointment;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ReviewAlreadyExistsException;
import appointmentscheduler.exception.UserNotAllowedToReviewServiceException;
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

    public ReviewDTOToReview(UserRepository userRepository, AppointmentRepository appointmentRepository,
                             ReviewRepository  reviewRepository){
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review convert(ReviewDTO reviewDTO) {
        Appointment appointment;
        Review review = new Review();
        if (appointmentRepository.findById(reviewDTO.getAppointmentId()).isPresent())
        {
            appointment = appointmentRepository.findById(reviewDTO.getAppointmentId()).get();
            boolean matchingClientId = appointment.getClient().getId() == reviewDTO.getClientId();
            boolean matchingAppointmentId = (appointment.getId() == reviewDTO.getAppointmentId());
            boolean existsReviewForAppointmentId =
                    reviewRepository.findByAppointmentId(appointment.getId()).isPresent();

            //only the user who had that appointment can review that specific appointment
            if (!matchingAppointmentId || !matchingClientId){
                throw new UserNotAllowedToReviewServiceException(String.format( "User with id %d is not authorized to" +
                                " write a review for appointment with id %d. This appointment was not assigned to " +
                                "them.",
                        reviewDTO.getClientId(), reviewDTO.getAppointmentId()));
            }
            if (existsReviewForAppointmentId)
            {
                throw new ReviewAlreadyExistsException(String.format("Appointment with id %d already has a review.",
                        reviewDTO.getAppointmentId()));
            }

        }
        else
        {
            throw new ResourceNotFoundException(String.format("Appointment with id %d not found.",
                    reviewDTO.getAppointmentId()));

        }

        if (userRepository.findById(reviewDTO.getClientId()).isPresent())
        {
            review.setClient(userRepository.findById(reviewDTO.getClientId()).get());
            review.setAppointment(appointment);
            review.setContent(reviewDTO.getContent());
        }
        return review;
    }

}
