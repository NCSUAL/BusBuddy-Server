package Java2Project.dto.response;
import Java2Project.domain.BusStopReview;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReviewResponse {
    private Long reviewId;// 리뷰 ID
    private Integer rating; //별점
    private String reviewText; //리뷰내용

    public static ReviewResponse of(BusStopReview review){
        return ReviewResponse
                .builder()
                .reviewId(review.getReviewId())
                .rating(review.getRating())
                .reviewText(review.getReviewText())
                .build();
    }
}
