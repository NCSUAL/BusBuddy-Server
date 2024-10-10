package Java2Project.service;

import Java2Project.domain.BusStop;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true) //연관관계의 주인이 아니면 읽기만 가능해야 함.
public class BusStopService {

    private final BusStopRepository busStopRepository;

    public BusStopService(BusStopRepository busStopRepository) {
        this.busStopRepository = busStopRepository;
    }

    public List<BusStop> findByStopName(String stopName) {
        List<BusStop> busStop = busStopRepository.findByStopName(stopName);
        if(busStop.isEmpty()) {
            throw new NotFoundBusStop("해당 버스정류장은 존재하지 않습니다.");
        }
        else{
            return busStop;
        }
    }


}
