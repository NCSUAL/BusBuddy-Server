package Java2Project.client;
import Java2Project.domain.BusStop;
import Java2Project.dto.busArrive.ArriveBusInfoResponse;
import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busRoute.BusRouteItem;
import Java2Project.dto.busRoute.BusRouteItemDto;
import Java2Project.dto.busStop.BusStopItem;
import Java2Project.dto.busStop.BusStopItemDto;
import Java2Project.dto.request.LocationRequest;
import Java2Project.exception.JsonProcessing;
import Java2Project.exception.NotFoundBusStop;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
@PropertySource("classpath:secret.properties")
public class NationalBusStopClient {

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

    public NationalBusStopClient(WebClient webClient, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    //노선 정보 요청
    public Mono<List<BusRouteItemDto>> busRouteRequest(BusStop busStop){
            log.info("*** 버스정류장 노선 조회 ***");

            //encode 중복 방지 URI 처리
            //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
            URI uri = UriComponentsBuilder.fromHttpUrl(busRoute)
                    .replaceQuery(String.format(
                            "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&nodeid=%s",
                            serviceKey,1, 100, "json"
                            , busStop.getCityCode()
                            , busStop.getBusStopId()
                    ))
                    .build(true) // 추가 인코딩 방지
                    .toUri();
            log.info("uri: {}" , uri);

            return webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(jsonNode -> jsonNode.path("response").path("body").path("items"))
                    //data flow processing
                    .flatMap(jsonNode -> {
                        try {
                            return Mono.just(objectMapper.treeToValue(jsonNode, BusRouteItem.class).getItem());
                        } catch (JsonProcessingException e) {
                            return Mono.error(new JsonProcessing("서버 오류가 발생하였습니다. 이유: Json 변환 실패"));
                        }
                    });
    }

    //버스정류장 위치 정보 요청
    public List<BusStopItemDto> busStopLocationRequest(LocationRequest locationRequest){
        try {
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

            log.info("** 요청 성공 **");
            //트리구조로 items까지 내려감
            JsonNode searchTree = getBusStop.path("response").path("body").path("items");

            return objectMapper.treeToValue(searchTree, BusStopItem.class).getItem();
        }
        catch (Exception e){
            log.info("조회 실패: {}",e.getMessage());
            throw new NotFoundBusStop("해당 범위에 버스정류장은 없습니다.");
        }
    }

    //정류장별로 도착 예정 정보 요청
    public List<BusArriveItemDto> busArriveRequest(BusStop busStop){
        //encode 중복 방지 URI 처리
        //replaceQuery를 사용하여 이미 인코딩된 쿼리 파라미터 전체를 URI로 설정
        URI uri = UriComponentsBuilder.fromHttpUrl(busArrive)
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
                .bodyToMono(ArriveBusInfoResponse.class) //형변환 -> Jackson
                .block(); //동기식으로 바꿈 추후에 Non-blocking 방식을 사용할 거임.

        log.info("id: {}, code: {}",busStop.getBusStopId(),busStop.getCityCode());

        return arriveBusInfoResponse.getResponse().getBody().getItems().getItem();
    }
}
