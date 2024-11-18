package Java2Project.controller;

import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.response.BusArriveProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.service.BusStopService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/api")
@Slf4j
public class BusStopController {

    @Autowired
    private BusStopService busStopService;

    //위도, 경도로 버스 정류장 찾기
    @PostMapping("/busStop")
    public ResponseEntity<List<BusStopResponse>> getBusStopByLatitudeAndLongitude(@Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.ok(
                        busStopService.findByLocation(locationRequest)
                                .stream()
                                .map(busStop -> BusStopResponse.of(busStop))
                                .toList()
        );
    }

    // 1. 위도, 경도로 버스 정류장 찾기
    // 2. 실시간 버스 도착 시간 가져오기
    @PostMapping("/bus")
    public ResponseEntity<BusArriveProvideResponse> getArriveBusAndBusStop(@Valid @RequestBody LocationRequest locationRequest) throws URISyntaxException {
        return ResponseEntity.ok(
                busStopService.arriveBusInfo(locationRequest)
        );
    }



}
