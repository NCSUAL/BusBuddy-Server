package Java2Project.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bus_stop_reviews")
public class BusStopReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;// 리뷰 ID

    //bus_stops 테이블과 연결되는 정류장 ID
    //연관된 부모 하나만 조회
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_stops_id")
    private BusStop busStop;

    @Column(nullable = false)
    private Integer rating; //별점

    private String ip;

    private String comment; //리뷰내용

    @Column(nullable = false)
    @CreationTimestamp
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now(); // 리뷰 작성 시간

    protected BusStopReview() {

    }

    @Builder
    public BusStopReview(Long reviewId, BusStop busStop, Integer rating, String comment, LocalDateTime createdAt,String ip) {
        setBusStop(busStop);
        this.reviewId = reviewId;
        this.busStop = busStop;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.ip = ip;
    }

    //연관관계 설정
    public void setBusStop(BusStop busStop) {
        this.busStop = busStop;
        busStop.getBusStopReviews().add(this);
    }
}
