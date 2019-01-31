package appointmentscheduler.service.review;

import appointmentscheduler.entity.review.Review;
import appointmentscheduler.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

   public Review add(Review review) {
       return reviewRepository.save(review);
   }
}
