package Java2Project.controller;

import java.util.List;
import Java2Project.domain.BusStopReview;
import Java2Project.service.BusStopReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/bus-stops/{busStopId}/reviews")
public class BusStopReviewController {

    private BusStopReviewService busStopReviewService;

    public BusStopReviewController(BusStopReviewService busStopReviewService) {
        this.busStopReviewService = busStopReviewService;
    }

    //Create Review API
    @PostMapping
    public ResponseEntity<BusStopReview> createReview(
            @PathVariable String busStopId,
            @RequestBody BusStopReview review
    ){
        review.setStopId(busStopId);
        BusStopReview saveReview = busStopReviewService.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveReview);
    }

    //Read API (특정 정류장의 리뷰 목록 조회
    @GetMapping
    public ResponseEntity<List<BusStopReview>> getReviewsByBusStopId(@PathVariable String busStopId){
        List<BusStopReview> reviews = busStopReviewService.getReviewsByBusStopId(busStopId);
        return ResponseEntity.ok(reviews);
    }

    //특정 리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<BusStopReview> getReviewById(@PathVariable Long reviewId){
        BusStopReview review = busStopReviewService.getReviewbyId(reviewId);
        return ResponseEntity.ok(review);
    }

}