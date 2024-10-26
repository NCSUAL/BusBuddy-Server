package Java2Project.controller;

import Java2Project.domain.BusStop;
import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.response.ArriveBusProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.service.BusStopService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.Location;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/api")
@Slf4j
public class BusStopController {

    @Autowired
    private BusStopService busStopService;

    //버스 정류장 이름으로 버스 정류장 찾기
    @GetMapping("/busStop/{name}")
    public ResponseEntity<List<BusStopResponse>> getBusStopByBusStopName(@PathVariable("name") String name){
//        List<BusStop> busStop = busStopService.findByStopName(name);
//
//        List<BusStopResponse> busStopResponses = new ArrayList<>();
//        for(BusStop bs : busStop){
//            busStopResponses.add(BusStopResponse.of(bs));
//        }
//
//        return ResponseEntity.ok().body(busStopResponses);
//        <---------------------------------------------------------->
//        Stream 적용

        return ResponseEntity.ok(
                busStopService.findByStopName(name)
                        .stream()
                        .map(BusStopResponse::of) //.map(BusStop -> BusStopResponse.of(BusStop))
                        .toList()
                );
    }

    //위도, 경도로 버스 정류장 찾기
    @PostMapping("/busStop")
    public ResponseEntity<BusStopResponse> getBusStopByLatitudeAndLongitude(@Valid @RequestBody LocationRequest locationRequest){
        return ResponseEntity.ok(
                BusStopResponse.of(
                        busStopService.findByLocation(locationRequest)
                )
        );
    }


    // 1. 위도, 경도로 버스 정류장 찾기
    // 2. 실시간 버스 도착 시간 가져오기
    @PostMapping("/bus")
    public ResponseEntity<ArriveBusProvideResponse> getArriveBusAndBusStop(@Valid @RequestBody LocationRequest locationRequest) throws URISyntaxException {
        return ResponseEntity.ok(
                busStopService.arriveBusInfo(locationRequest)
        );
    }

}
