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

    @Override
    public Review convert(ReviewDTO reviewDTO) {
        Appointment appointment;
        Review review = new Review();

        //verify that there is an appointment existing with the given appointment id
        if (appointmentRepository.findById(reviewDTO.getAppointmentId()).isPresent())
        {
            appointment = appointmentRepository.findById(reviewDTO.getAppointmentId()).get();
            boolean matchingClientId = appointment.getClient().getId() == reviewDTO.getClientId();
            boolean matchingAppointmentId = (appointment.getId() == reviewDTO.getAppointmentId());
            boolean existsReviewForAppointmentId =
                    reviewRepository.findByAppointmentId(appointment.getId()).isPresent();

            //verify that the client id and the appointment id of the request match with the database
            if (matchingClientId && matchingAppointmentId)
            {
                if (existsReviewForAppointmentId)
                {
                    throw new ReviewAlreadyExistsException(String.format("Appointment with id %d already has a review.",
                            reviewDTO.getAppointmentId()));
                }
                //if there is no review for the given appointment id, then add the appointment to the review
                else{
                    if (userRepository.findById(reviewDTO.getClientId()).isPresent())
                    {
                    review.setClient(userRepository.findById(reviewDTO.getClientId()).get());
                    review.setAppointment(appointment);

                    }
                }
            }
            else
            {
                throw new UserNotAllowedToReviewServiceException(String.format( "Client with id %d is not authorized to write a review for appointment with id %d.",
                        reviewDTO.getClientId(), reviewDTO.getAppointmentId()));
            }

        //there is no appointment that exists with the current appointment id
        } else {
            throw new ResourceNotFoundException(String.format("Appointment with id %d not found.",
                    reviewDTO.getAppointmentId()));
        }

        review.setContent(reviewDTO.getContent());
        return review;
    }

}
