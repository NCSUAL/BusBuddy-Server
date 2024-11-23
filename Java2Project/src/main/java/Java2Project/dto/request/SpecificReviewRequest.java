package Java2Project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SpecificReviewRequest(
       @NotNull(message = "reviewId는 포함되어야 합니다.") Long reviewId,
       @NotNull(message = "별점(Rated)은 포함되어야 합니다!")
       @Min(value = 0,message = "최소 평점은 0점입니다!")
       @Max(value = 5, message = "최대 평점은 5점입니다!") Integer rating,
       @NotNull(message = "댓글(comment)은 포함되어야 합니다!") String comment
){}
