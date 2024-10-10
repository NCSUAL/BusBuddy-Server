package Java2Project.controller;

import Java2Project.domain.BusStop;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.service.BusStopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api")
@Slf4j
public class BusStopController {

    @Autowired
    private BusStopService busStopService;

    //버스 정류장 이름으로 조회 -> 버스 정류장 찾기
    @GetMapping("/busStop/{name}")
    public ResponseEntity<List<BusStopResponse>> getBusStop(@PathVariable("name") String name){
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

}
