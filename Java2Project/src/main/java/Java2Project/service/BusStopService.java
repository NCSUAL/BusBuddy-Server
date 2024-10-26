package Java2Project.service;

import Java2Project.domain.BusStop;
import Java2Project.dto.arriveBus.ItemDto;
import Java2Project.dto.process.ArriveBusDto;
import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.response.ArriveBusInfoResponse;

import Java2Project.dto.response.ArriveBusProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true) //연관관계의 주인이 아니면 읽기만 가능해야 함.
public class BusStopService {

    private final BusStopRepository busStopRepository;

    private final WebClient webClient; //Rest Api 요청

    public BusStopService(BusStopRepository busStopRepository,WebClient webClient) {
        //dependency Injection
        this.webClient = webClient;
        this.busStopRepository = busStopRepository;
    }

    public List<BusStop> findByStopName(String stopName) {
        List<BusStop> busStop = busStopRepository.findByStopName(stopName);
        if(busStop.isEmpty()) {
            throw new NotFoundBusStop("해당 버스정류장이 없습니다.");
        }
        else{
            return busStop;
        }
    }

    public BusStop findByLocation(LocationRequest locationRequest){
        //위도 경도로 버스 정류장의 정보를 가져옴
        Optional<BusStop> busStop = busStopRepository.findByLatitudeAndLongitudeWithOption(locationRequest.latitude(), locationRequest.longitude());
        if(busStop.isPresent()) {
            return busStop.get();
        }
        else{
            throw new NotFoundBusStop("해당 버스정류장이 없습니다.");
        }
    }

    //정류소별도착예정정보 목록 조회
    /*
    정류소별로 실시간 도착예정정보 및 운행정보 목록을 조회한다.
    */

    public ArriveBusProvideResponse arriveBusInfo(LocationRequest locationRequest){
        //위도 경도로 버스 정류장의 정보를 가져옴
        BusStop busStop = findByLocation(locationRequest);

        //encode 중복 방지 URI 처리
        //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
        URI uri = UriComponentsBuilder.fromHttpUrl("https://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList")
                .replaceQuery(String.format(
                        "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&nodeId=%s",
                        "d",1, 10, "json"
                        , busStop.getCityCode()
                        , busStop.getBusStopId()
                ))
                .build(true) // 추가 인코딩 방지
                .toUri();


        ArriveBusInfoResponse arriveBusInfoResponse = webClient
                .get() //get 요청
                .uri(uri)
                .retrieve() //indication

                //데이터 받음
                .bodyToMono(ArriveBusInfoResponse.class)//형변환 -> Jackson
                .block(); //동기식으로 바꿈 추후에 Non-block 방식을 사용할거임.

        log.info("{}",arriveBusInfoResponse.toString());


        //데이터 가공
        List<ItemDto> items = arriveBusInfoResponse.getResponse().getBody().getItems().getItem();


        //객체 정렬
        Collections.sort(items, new Comparator<ItemDto>() {
            @Override
            public int compare(ItemDto o1, ItemDto o2) {
                return o1.getArrtime() - o2.getArrtime();
            }
        });

        if(items.size()>3){
            items.subList(0,3);
        }

        return ArriveBusProvideResponse.builder()
                .busStopResponse(BusStopResponse.of(busStop))
                //데이터 가공 2
                .items(items.stream().map(itemDto -> ArriveBusDto.of(itemDto)).toList())
                .build();
    }



}
