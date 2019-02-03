package appointmentscheduler.controller.rest;

import appointmentscheduler.converters.review.ReviewDTOToReview;
import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.service.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${rest.api.path}/reviews")
public class ReviewController extends IRestController<Review, ReviewDTO>{

    @Autowired
    private ReviewDTOToReview reviewConverter;

    @Autowired
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService, ReviewDTOToReview reviewConverter) {
        this.reviewService = reviewService;
        this.reviewConverter = reviewConverter;
    }

    @Override
    List<Review> findAll() {
        return null;
    }

    Review findById(long id) {
        return null;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public Review add(@RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setClientId(getUserId());
        Review review = reviewConverter.convert(reviewDTO);
        return reviewService.add(review);
    }

    @Override
    Review update(long id, ReviewDTO reviewDTO) {
        return null;
    }

    @Override
    ResponseEntity delete(long id) {
        return null;
    }
}
