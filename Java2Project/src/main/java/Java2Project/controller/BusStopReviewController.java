package Java2Project.controller;
import Java2Project.domain.BusStop;
import Java2Project.domain.BusStopReview;
import Java2Project.dto.request.ReviewRequest;
import Java2Project.dto.response.ReviewResponse;
import Java2Project.service.BusStopReviewService;
import Java2Project.service.BusStopService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/review")
public class BusStopReviewController {

    private BusStopService busStopService;
    private BusStopReviewService busStopReviewService;

    public BusStopReviewController(BusStopReviewService busStopReviewService, BusStopService busStopService) {
        this.busStopService = busStopService;
        this.busStopReviewService = busStopReviewService;
    }

    //Create Review API
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest){
        ReviewResponse saveReview = busStopService.addComment(reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveReview);
    }

    //Read API (특정 정류장의 리뷰 목록 조회)
    //특정 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<BusStopReview> getReviewById(@PathVariable Long reviewId){
        BusStopReview review = busStopReviewService.getReviewbyId(reviewId);
        return ResponseEntity.ok(review);
    }

}