package Java2Project.service;
import Java2Project.domain.BusStop;
import Java2Project.domain.BusStopReview;
import Java2Project.dto.request.ReviewRequest;
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


    public BusStopReviewService(BusStopReviewRepository busReviewRepository, BusStopRepository busStopRepository){
        this.busStopRepository = busStopRepository;
        this.busReviewRepository = busReviewRepository;
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
                .reviewText(reviewRequest.comment())
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

}
