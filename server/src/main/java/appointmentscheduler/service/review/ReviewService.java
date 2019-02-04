package appointmentscheduler.service.review;

import appointmentscheduler.entity.review.Review;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review add(Review review) {
        return reviewRepository.save(review);
    }

    public Review findByAppointmentId(long appointmentId) {
        return reviewRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Review for appointment id %d not " +
                                "found.",
                        appointmentId)));
    }
}
