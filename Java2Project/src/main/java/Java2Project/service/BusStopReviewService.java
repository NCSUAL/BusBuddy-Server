package Java2Project.service;
import Java2Project.domain.BusStop;
import Java2Project.domain.BusStopReview;
import Java2Project.dto.request.ReviewRequest;
import Java2Project.dto.request.SpecificReviewRequest;
import Java2Project.dto.response.ReviewResponse;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import Java2Project.repository.BusStopReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BusStopReviewService {
    private final BusStopRepository busStopRepository;

    private final BusStopReviewRepository busReviewRepository;
    private final BusStopReviewRepository busStopReviewRepository;


    public BusStopReviewService(BusStopReviewRepository busReviewRepository, BusStopRepository busStopRepository, BusStopReviewRepository busStopReviewRepository){
        this.busStopRepository = busStopRepository;
        this.busReviewRepository = busReviewRepository;
        this.busStopReviewRepository = busStopReviewRepository;
    }

    //댓글 추가
    public ReviewResponse addComment(ReviewRequest reviewRequest){
        //BusStopId로 정류장 조회
        BusStop busStop = busStopRepository.findById(reviewRequest.busStopId())
                .stream().findFirst().orElseThrow(() -> new NotFoundBusStop("해당 버스 정류장은 없습니다."));

        return ReviewResponse.of(busReviewRepository.save(BusStopReview
                .builder()
                .busStop(busStop)
                .rating(reviewRequest.Rated())
                .comment(reviewRequest.comment())
                .build()));
    }

    //특정 정류장 리뷰 조회
    public List<ReviewResponse> inquireAllReviews(String busStopId){
        return busStopRepository.findById(busStopId)
                .orElseThrow(() -> new NotFoundBusStop("해당 버스 정류장은 없습니다."))
                .getBusStopReviews()
                .stream()
                .map(ReviewResponse::of)
                .toList();
    }

    //특정 리뷰 수정
    public ReviewResponse updateReview(SpecificReviewRequest specificReviewRequest){
        BusStopReview busStopReview = busStopReviewRepository.findById(specificReviewRequest.reviewId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundBusStop("해당 리뷰를 찾을 수 없습니다."));

        busStopReview.setComment(specificReviewRequest.comment());

        return ReviewResponse.of(busStopReview);
    }

}
