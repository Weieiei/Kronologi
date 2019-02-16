package appointmentscheduler.service.review;

import appointmentscheduler.entity.review.Review;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Map<String, String> add(Review review) {
        try{
        reviewRepository.save(review);
        return message ("Successfully added review");
        }
        catch (Exception e) {
            return message(e.getMessage());
        }
    }

    public Optional <Review> findByAppointmentId(long appointmentId) {
        return reviewRepository.findByAppointmentId(appointmentId);
    }


    private Map<String, String> message(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }
}
