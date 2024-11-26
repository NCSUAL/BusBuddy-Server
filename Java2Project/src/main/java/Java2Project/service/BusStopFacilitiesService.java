package Java2Project.service;
import Java2Project.domain.BusStop;
import Java2Project.domain.Facilities;
import Java2Project.dto.request.FacilitiesRequest;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import Java2Project.repository.FacilitiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BusStopFacilitiesService {
    private final BusStopRepository busStopRepository;
    private final FacilitiesRepository facilitiesRepository;

    public BusStopFacilitiesService(BusStopRepository busStopRepository, FacilitiesRepository facilitiesRepository) {
        this.busStopRepository = busStopRepository;
        this.facilitiesRepository = facilitiesRepository;
    }

    private Long calcurator(boolean condition, Long value){
        return condition? value +1 :  value;
    }

    public Facilities inquiryFacilities(String busStopId){
        return busStopRepository.findById(busStopId).orElseThrow(() -> new NotFoundBusStop("해당 버스 정류장은 없습니다.")).getFacilities();
    }

    public Facilities addFacilities(String busStopId, FacilitiesRequest facilitiesRequest){
        BusStop busStop = busStopRepository.findById(busStopId).orElseThrow(() -> new NotFoundBusStop("해당 버스 정류장은 없습니다."));
        Optional<Facilities> facilities = facilitiesRepository.findByBusStop(busStop);
        if(facilities.isPresent()){
            Facilities facilitiesGet = facilities.get();
            facilitiesGet.setFacilityIndex(facilitiesGet.getFacilityIndex()+1);
            facilitiesGet.setHasCeiling(calcurator(facilitiesRequest.hasCeiling(), facilitiesGet.getHasCeiling()));
            facilitiesGet.setHasCharger(calcurator(facilitiesRequest.hasCharger(), facilitiesGet.getHasCharger()));
            facilitiesGet.setHasChair(calcurator(facilitiesRequest.hasChair(), facilitiesGet.getHasChair()));
            facilitiesGet.setHasWiFi(calcurator(facilitiesRequest.hasWiFi(), facilitiesGet.getHasWiFi()));
            facilitiesGet.setHasAirConditioning(calcurator(facilitiesRequest.hasAirConditioning(), facilitiesGet.getHasAirConditioning()));
            facilitiesGet.setHasHeatedChair(calcurator(facilitiesRequest.hasHeatedChair(), facilitiesGet.getHasHeatedChair()));
            facilitiesGet.setHasArrivalInfoSystem(calcurator(facilitiesRequest.hasArrivalInfoSystem(), facilitiesGet.getHasArrivalInfoSystem()));
            return facilitiesGet;
        }
        else{
            Facilities fac = Facilities
                    .builder()
                    .facilityIndex(1L)
                    .hasCharger(facilitiesRequest.hasCharger() ? 1L : 0L)
                    .hasWiFi(facilitiesRequest.hasWiFi() ? 1L : 0L)
                    .hasAirConditioning(facilitiesRequest.hasAirConditioning() ? 1L : 0L)
                    .hasChair(facilitiesRequest.hasChair() ? 1L : 0L)
                    .hasCeiling(facilitiesRequest.hasCeiling() ? 1L : 0L)
                    .hasHeatedChair(facilitiesRequest.hasHeatedChair() ? 1L : 0L)
                    .hasArrivalInfoSystem(facilitiesRequest.hasArrivalInfoSystem() ? 1L : 0L)
                    .build();
            fac.setBusStop(busStop);
            return facilitiesRepository.save(fac);
        }
    }
}
