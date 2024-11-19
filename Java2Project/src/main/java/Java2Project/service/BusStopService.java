package Java2Project.service;

import Java2Project.domain.BusStop;
import Java2Project.dto.arriveBus.ArriveBusItemDto;
import Java2Project.dto.busStop.BusStopItemDto;
import Java2Project.dto.busStop.BusStopItems;
import Java2Project.dto.process.ArriveBusDto;
import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.response.ArriveBusInfoResponse;

import Java2Project.dto.response.ArriveBusProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true) //연관관계의 주인이 아니면 읽기만 가능해야 함.
@PropertySource("classpath:uri.properties")
@PropertySource("classpath:secret.properties")

public class BusStopService {

    private final BusStopRepository busStopRepository;

    //비동기 요청을 위한 webClient
    private final WebClient webClient;

    //동기 요청을 위한 restTemplate
    private final RestTemplate restTemplate;

    //버스 정류장 도착 정보 URI
    @Value("${arriveBusStop}")
    private String arriveBusStop;

    //버스 정류장 위치 정보 URI
    @Value("${locationBusStop}")
    private String locationBusStop;

    //공공데이터 serviceKey
    @Value("${serviceKey}")
    private String serviceKey;

    private final ObjectMapper objectMapper;

    public BusStopService(BusStopRepository busStopRepository, WebClient webClient, RestTemplate restTemplate,ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.busStopRepository = busStopRepository;
        this.restTemplate = restTemplate;
        this.objectMapper =objectMapper;
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

    //공공데이터 api 요청 받고 데이터를 저장할 수 있음.
    @Transactional
    public List<BusStop> findByLocation(LocationRequest locationRequest){
        //위도 경도로 버스 정류장의 정보를 가져옴
        log.info("*** DB에서 데이터 조회 ***");
        List<BusStop> busStop = busStopRepository.findByLatitudeAndLongitudeWithOption(locationRequest.latitude(), locationRequest.longitude());

        if(busStop.isEmpty()){
            log.info("----- DB에 존재하지 않음 -----");
            log.info("*** 정류장을 api로 요청 ***");

            //공공데이터로 요청
            //encode 중복 방지 URI 처리
            //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
            URI uri = UriComponentsBuilder.fromHttpUrl(locationBusStop)
                    .replaceQuery(String.format(
                            "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&gpsLati=%s&gpsLong=%s",
                            serviceKey,1, 5, "json"
                            ,  locationRequest.latitude()
                            ,  locationRequest.longitude()
                    ))
                    .build(true) // 추가 인코딩 방지
                    .toUri();

            log.info("uri: {}" , uri);
            //버스 정류장 정보 요청
            JsonNode getBusStop = restTemplate.getForObject(uri, JsonNode.class);

            try{
                log.info("** 요청 성공 **");
                //트리구조로 items까지 내려감
                JsonNode searchTree = getBusStop.path("response").path("body").path("items");

                List<BusStopItemDto> items = objectMapper.treeToValue(searchTree, BusStopItems.class).getItem();
                log.info("Data: {}",items);

                if(items.isEmpty()){
                    throw new NotFoundBusStop("해당 범위에 버스정류장은 없습니다.");
                }

                log.info("* 데이터 필터링 진행 *");
                //list의 첫값과 nodeno가 같은 원소를 필터링하고 리턴
                List<BusStop> busStops =  items
                        .stream()
                        .filter(busStopItemDto -> items.get(0).getNodeno() == busStopItemDto.getNodeno())
                        .map(busStopItemDto -> BusStop.of(busStopItemDto))
                        .toList();

                log.info("* 데이터 필터링 성공: {}", busStops);

                log.info("*** 데이터 저장 ***");
                busStopRepository.saveAll(busStops);

                log.info("모든 작업이 정상적으로 처리 됨");
                return busStops;

            }
            catch (Exception e){
                log.info("조회 실패: {}",e.getMessage());
                throw new NotFoundBusStop("해당 범위에 버스정류장은 없습니다.");
            }

        }
        else{
            busStop.sort(new Comparator<BusStop>() {
                @Override
                public int compare(BusStop o1, BusStop o2) {
                    double a1 = Math.abs(locationRequest.latitude() - o1.getLatitude().doubleValue()) +
                            Math.abs(locationRequest.latitude() - o1.getLongitude().doubleValue());
                    double a2 = Math.abs(locationRequest.latitude() - o2.getLatitude().doubleValue()) +
                            Math.abs(locationRequest.latitude() - o2.getLongitude().doubleValue());
                    return Double.compare(a1,a2);
                }
            });

            return busStop;

        }
    }

    //정류소별도착예정정보 목록 조회
    /*
    정류소별로 실시간 도착예정정보 및 운행정보 목록을 조회한다.
    */

    public ArriveBusProvideResponse arriveBusInfo(LocationRequest locationRequest){
        //위도 경도로 버스 정류장의 정보를 가져옴
        BusStop busStop = findByLocation(locationRequest).get(0);

        log.info("id: {}, code: {}",busStop.getBusStopId(),busStop.getCityCode());
        //encode 중복 방지 URI 처리
        //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
        URI uri = UriComponentsBuilder.fromHttpUrl(arriveBusStop)
                .replaceQuery(String.format(
                        "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&nodeId=%s",
                        serviceKey,1, 10, "json"
                        , busStop.getCityCode()
                        , busStop.getBusStopId()
                ))
                .build(true) // 추가 인코딩 방지
                .toUri();

        ArriveBusInfoResponse arriveBusInfoResponse = webClient
                .get() //get 요청
                .uri(uri)
                .retrieve() //RequestAndConfirm

                //데이터 받음
                .bodyToMono(ArriveBusInfoResponse.class)//형변환 -> Jackson
                .block(); //동기식으로 바꿈 추후에 Non-blocking 방식을 사용할거임.

        //데이터 가공
        List<ArriveBusItemDto> items = arriveBusInfoResponse.getResponse().getBody().getItems().getItem();


        //객체 정렬
        Collections.sort(items, new Comparator<ArriveBusItemDto>() {
            @Override
            public int compare(ArriveBusItemDto o1, ArriveBusItemDto o2) {
                return o1.getArrtime() - o2.getArrtime();
            }
        });

        return ArriveBusProvideResponse.builder()
                .busStopResponse(BusStopResponse.of(busStop))
                .items(items.stream().map(arriveBusItemDto -> ArriveBusDto.of(arriveBusItemDto)).toList())
                .build();
    }
}
