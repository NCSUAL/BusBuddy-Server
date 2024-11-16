package Java2Project.repository;

import Java2Project.domain.BusStopReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusStopReviewRepository extends JpaRepository<BusStopReview, Long> {
    List<BusStopReview> findBySopId(String stopId); //특정 정류장에 대한 모든 리뷰 조회
}
