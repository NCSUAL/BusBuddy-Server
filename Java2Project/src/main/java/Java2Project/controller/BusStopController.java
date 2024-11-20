package Java2Project.controller;
import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.response.BusArriveProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.service.BusStopService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class BusStopController {

    @Autowired
    private BusStopService busStopService;

    //위도, 경도로 버스 정류장 찾기
    @PostMapping("/busStop")
    public ResponseEntity<Mono<List<BusStopResponse>>> getBusStopByLatitudeAndLongitude(@Valid @RequestBody LocationRequest locationRequest){

        return ResponseEntity.ok(
                        busStopService.busStopInfo(locationRequest)
                                .map(busStops -> busStops.stream().map(BusStopResponse::of).toList())
        );
    }

    // 1. 위도, 경도로 버스 정류장 찾기
    // 2. 실시간 버스 도착 시간 가져오기
    @PostMapping("/bus")
    public ResponseEntity<Mono<BusArriveProvideResponse>> getArriveBusAndBusStop(@Valid @RequestBody LocationRequest locationRequest) throws URISyntaxException {
        return ResponseEntity.ok(
                busStopService.arriveBusInfo(locationRequest)
        );
    }



}
