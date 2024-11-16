package Java2Project.service;
import Java2Project.domain.BusStopReview;
import Java2Project.repository.BusStopReviewRepository;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BusStopReviewService {
    private final BusStopReviewRepository reviewRepository;


    public BusStopReviewService(BusStopReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public BusStopReview createReview(BusStopReview review){
        if (review == null || review.getReviewText().isEmpty()){
            throw new IllegalArgumentException("Review cannot be empty");
        }
        return reviewRepository.save(review);
    }
}
