package Java2Project.domain;

import jakarta.persistence.*;
import Java2Project.domain.BusStop;
import lombok.Data;

@Data
@Entity
@Table(name = "bus_stop_reviews")

public class BusStopReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;// 리뷰 ID

    @Column(nullable = false)
    private String stopId;// bus_stops 테이블과 연결되는 정류장 ID

    @Column(nullable = false)
    private String userId;// user테이블과 연결되는 사용자 ID

    @Column(nullable = false)
    private Integer rating; //별점

    private String reviewText; //리뷰내용

    @Column(nullable = false)
    private java.time.LocalDateTime createdAt = java.time.LocalDateTime.now(); // 리뷰 작성 시간


    //Getter and Setter for 'busStopId'
    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    //Getter and Setter for 'reveiwtext'
    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    //Getter and Setter for 'reviewId'
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    //Getter and Setter for 'rating'
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    //Getter and Setter for 'busStopId'
    public String getBusStopId(){
        return stopId;
    }

    public void setBusStopId(String busStopId){
        this.stopId = busStopId;
    }
}
