package Java2Project.repository;

import Java2Project.domain.BusStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop,Long> {

    //버스 정류장 이름으로 BusStop 객체 불러옴
    List<BusStop> findByStopName(String stopName);
}
