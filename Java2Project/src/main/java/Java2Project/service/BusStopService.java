package Java2Project.service;

import Java2Project.domain.BusStop;
import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busRoute.BusRouteItem;
import Java2Project.dto.busRoute.BusRouteItemDto;
import Java2Project.dto.busStop.BusStopItemDto;
import Java2Project.dto.busStop.BusStopItem;
import Java2Project.dto.process.ArriveBusDto;
import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.busArrive.ArriveBusInfoResponse;

import Java2Project.dto.response.BusArriveProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestClientException;
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
    @Value("${busArrive}")
    private String busArrive;

    //버스 정류장 위치 정보 URI
    @Value("${locationBusStop}")
    private String locationBusStop;

    //버스 정류장 노선 정보 URI
    @Value("${busRoute}")
    private String busRoute;

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

    //노선 정보 조회
    public List<BusRouteItemDto> busRouteInfo(BusStop busStop){
        log.info("*** 버스정류장 노선 조회 ***");

        //encode 중복 방지 URI 처리
        //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
        URI uri = UriComponentsBuilder.fromHttpUrl(busRoute)
                .replaceQuery(String.format(
                        "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&nodeId=%s",
                        serviceKey,1, 10, "json"
                        , busStop.getCityCode()
                        , busStop.getBusStopId()
                ))
                .build(true) // 추가 인코딩 방지
                .toUri();

        log.info("uri: {}" , uri);

        try{
            JsonNode requestBusRoute = restTemplate.getForObject(uri, JsonNode.class);

            requestBusRoute = requestBusRoute.path("response").path("body").path("items");

            List<BusRouteItemDto> busRouteItemDtos = objectMapper.treeToValue(requestBusRoute, BusRouteItem.class).getItem();

            log.info("{}",busRouteItemDtos);
            return busRouteItemDtos;
        }
        catch (RestClientException e){
            throw new RestClientException("");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            try{
                //버스 정류장 정보 요청
                JsonNode getBusStop = restTemplate.getForObject(uri, JsonNode.class);

                log.info("** 요청 성공 **");
                //트리구조로 items까지 내려감
                JsonNode searchTree = getBusStop.path("response").path("body").path("items");

                List<BusStopItemDto> items = objectMapper.treeToValue(searchTree, BusStopItem.class).getItem();
                log.info("Data: {}",items);

                if(items.isEmpty()){
                    throw new NotFoundBusStop("해당 범위에 버스정류장은 없습니다.");
                }

                log.info("* 데이터 필터링 진행 *");
                //list의 첫값과 nodeno가 같은 원소를 필터링하고 리턴한다
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



    //정류소별 도착 예정 정보 목록 조회
    /*
    정류소별로 실시간 도착 예정 정보 및 운행정보 목록,노선 정보를 조회한다.
    */
    @Transactional
    public BusArriveProvideResponse arriveBusInfo(LocationRequest locationRequest){
        //데이터 가공
        List<ArriveBusDto> arriveBusDtos = new ArrayList<>();

        List<BusArriveItemDto> items = new ArrayList<>();

        List<BusRouteItemDto> busRouteItemDtos = new ArrayList<>();

        log.info("-- 정류소 도착 예정 정보 동기식으로 공공데이터에 요청 --");
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        //위도 경도로 버스 정류장의 정보를 가져옴
        List<BusStop> busStop = findByLocation(locationRequest);

        busStop.stream().forEach(busStop1 ->
        {
            //노선 정보들을 가져옴
            busRouteItemDtos.addAll(busRouteInfo(busStop1));

            //encode 중복 방지 URI 처리
            //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
            URI uri = UriComponentsBuilder.fromHttpUrl(busArrive)
                    .replaceQuery(String.format(
                            "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&nodeId=%s",
                            serviceKey,1, 10, "json"
                            , busStop1.getCityCode()
                            , busStop1.getBusStopId()
                    ))
                    .build(true) // 추가 인코딩 방지
                    .toUri();


            ArriveBusInfoResponse arriveBusInfoResponse = webClient
                    .get() //get 요청
                    .uri(uri)
                    .retrieve() //RequestAndConfirm

                    //데이터 받음
                    .bodyToMono(ArriveBusInfoResponse.class) //형변환 -> Jackson
                    .block(); //동기식으로 바꿈 추후에 Non-blocking 방식을 사용할 거임.

            log.info("id: {}, code: {}",busStop1.getBusStopId(),busStop1.getCityCode());

            items.addAll(arriveBusInfoResponse.getResponse().getBody().getItems().getItem());
        });
        stopWatch.stop();

        //객체 정렬
        Collections.sort(items, new Comparator<BusArriveItemDto>() {
            @Override
            public int compare(BusArriveItemDto o1, BusArriveItemDto o2) {
                return o1.getArrtime() - o2.getArrtime();
            }
        });

        log.info("총 걸린 시간: {}",stopWatch.getTotalTimeMillis());

        log.info("{}",items);
        log.info("{}",busRouteItemDtos);

        for(BusArriveItemDto busArriveItemDto: items){
            for(BusRouteItemDto busRouteItemDto : busRouteItemDtos){
                if( busArriveItemDto.getRouteid().equals(busRouteItemDto.getRouteid())){
                    arriveBusDtos.add(ArriveBusDto.of(busArriveItemDto,busRouteItemDto));
                }
            }
        }


        return BusArriveProvideResponse.builder()
                .busStopResponse(busStop.stream().map(busStop1 -> BusStopResponse.of(busStop1)).toList())
                .items(arriveBusDtos)
                .build();
    }
}
