package Java2Project.service;
import Java2Project.domain.BusStopReview;
import Java2Project.repository.BusStopReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BusStopReviewService {
    private final BusStopReviewRepository reviewRepository;


    public BusStopReviewService(BusStopReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    //CREATE
    public BusStopReview createReview(BusStopReview review){
        if (review == null || review.getReviewText().isEmpty()){
            throw new IllegalArgumentException("Review cannot be empty");
        }
        return reviewRepository.save(review);
    }

    //READ(특정 정류장의 리뷰 목록 조회

    //특정 리뷰 상세조회
    public BusStopReview getReviewbyId(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(()->new IllegalArgumentException("Review not found with ID: " + reviewId));
    }
}
