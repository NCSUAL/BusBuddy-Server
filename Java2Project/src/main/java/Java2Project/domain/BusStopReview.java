package Java2Project.domain;

import jakarta.persistence.*;
import Java2Project.domain.BusStop;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "bus_stop_reviews")
public class BusStopReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;// 리뷰 ID

    // bus_stops 테이블과 연결되는 정류장 ID
    //연관된 부모 하나만 조회
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "busStopId")
    private BusStop stopId;

    @Column(nullable = false)
    private String userId;// user테이블과 연결되는 사용자 ID

    @Column(nullable = false)
    private Integer rating; //별점

    private String reviewText; //리뷰내용

    @Column(nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now(); // 리뷰 작성 시간

    protected BusStopReview() {

    }

    @Builder
    public BusStopReview(Long reviewId, BusStop stopId, String userId, Integer rating, String reviewText, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.stopId = stopId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }
}
