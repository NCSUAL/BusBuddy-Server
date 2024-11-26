package Java2Project.controller;
import Java2Project.dto.request.ReviewRequest;
import Java2Project.dto.request.SpecificReviewRequest;
import Java2Project.dto.response.ReviewResponse;
import Java2Project.service.BusStopReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/review")
public class BusStopReviewController {

    private final BusStopReviewService busStopReviewService;

    public BusStopReviewController(BusStopReviewService busStopReviewService) {
        this.busStopReviewService = busStopReviewService;
    }

    //리뷰 추가
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest reviewRequest){
        ReviewResponse saveReview = busStopReviewService.addComment(reviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveReview);
    }

    //특정 정류장의 리뷰 목록 조회
    @GetMapping("/{busStopId}")
    public ResponseEntity<List<ReviewResponse>> inquireAllReviews(@PathVariable("busStopId") String busStopId){
        return ResponseEntity.ok().body(busStopReviewService.inquireAllReviews(busStopId));
    }

    //특정 리뷰 수정
    @PutMapping
    public ResponseEntity<ReviewResponse> updateReview(@Valid @RequestBody SpecificReviewRequest specificReviewRequest){
        return ResponseEntity.ok().body(busStopReviewService.updateReview(specificReviewRequest));
    }

    //특정 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId){
        busStopReviewService.deleteReview(reviewId);
        return ResponseEntity.ok().body("삭제가 완료되었습니다.");
    }

}