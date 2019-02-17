package appointmentscheduler.controller.rest;

import appointmentscheduler.annotation.LogREST;
import appointmentscheduler.converters.review.ReviewDTOToReview;
import appointmentscheduler.dto.review.ReviewDTO;
import appointmentscheduler.entity.review.Review;
import appointmentscheduler.exception.ResourceNotFoundException;
import appointmentscheduler.exception.ReviewAlreadyExistsException;
import appointmentscheduler.exception.UserNotAllowedToReviewServiceException;
import appointmentscheduler.service.review.ReviewService;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/${rest.api.path}/reviews")
public class ReviewController extends AbstractController{

    @Autowired
    private ReviewDTOToReview reviewConverter;

    @Autowired
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService, ReviewDTOToReview reviewConverter) {
        this.reviewService = reviewService;
        this.reviewConverter = reviewConverter;
    }

    @GetMapping("/{appointmentId}")
    Optional<Review> findByAppointmentId(@PathVariable long appointmentId) {
        return reviewService.findByAppointmentId(appointmentId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CLIENT')")
    public ResponseEntity<Map<String, String>> add(@RequestBody ReviewDTO reviewDTO) {
        reviewDTO.setClientId(getUserId());
        try{
            Review review = reviewConverter.convert(reviewDTO);
            return ResponseEntity.ok(reviewService.add(review));
        }catch (ReviewAlreadyExistsException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (UserNotAllowedToReviewServiceException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (ResourceNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
