package Java2Project.client;
import Java2Project.domain.BusStop;
import Java2Project.dto.busArrive.BusArriveItem;
import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busLocation.BusLocationItem;
import Java2Project.dto.busLocation.BusLocationItemDto;
import Java2Project.dto.busRoute.BusRouteItem;
import Java2Project.dto.busRoute.BusRouteItemDto;
import Java2Project.dto.busStop.BusStopItem;
import Java2Project.dto.busStop.BusStopItemDto;
import Java2Project.dto.request.BusRequest;
import Java2Project.dto.request.LocationRequest;
import Java2Project.exception.JsonProcessing;
import Java2Project.utils.JsonNodeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@PropertySource("classpath:uri.properties")
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

    //버스 위치 정보 URI
    @Value(("${busLocation}"))
    private String busLocation;

    //공공데이터 serviceKey
    @Value("${serviceKey}")
    private String serviceKey;

    private final ObjectMapper objectMapper;

    public NationalBusStopClient(WebClient webClient, ObjectMapper objectMapper,RestTemplate restTemplate) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
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

            log.info("노선 정보 uri: {}" , uri);

            return webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(jsonNode -> jsonNode.path("response").path("body").path("items"))
                    //data flow processing
                    .flatMap(jsonNode -> {
                        try {
                            if(JsonNodeUtil.exceptionEmpty(jsonNode).isPresent()){
                                return Mono.just(Collections.emptyList());
                            }
                            else{
                                return Mono.just(objectMapper.treeToValue(jsonNode, BusRouteItem.class).getItem());
                            }
                        } catch (JsonProcessingException e) {
                            return Mono.error(new JsonProcessing("서버 오류가 발생하였습니다. 이유: 노선 Json 변환 실패"));
                        }
                    })
                    ;
    }

    //버스정류장 위치 정보 요청
    public Mono<List<BusStopItemDto>> busStopLocationRequest(LocationRequest locationRequest){
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

            log.info("버스정류장 위치 uri: {}" , uri);

            return webClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(jsonNode -> jsonNode.path("response").path("body").path("items"))
                    .flatMap(jsonNode -> {
                        try {
                            if(JsonNodeUtil.exceptionEmpty(jsonNode).isPresent()){
                                return Mono.just(Collections.emptyList());
                            }
                            else{
                                return Mono.just(objectMapper.treeToValue(jsonNode,BusStopItem.class).getItem());
                            }
                        } catch (JsonProcessingException e) {
                            return Mono.error(new JsonProcessing("서버 오류가 발생하였습니다. 이유: 버스정류장 Json 변환 실패"));
                        }
                    });
        }

    //정류장별로 도착 예정 정보 요청
    public Mono<List<BusArriveItemDto>> busArriveRequest(BusStop busStop) {
        URI uri = UriComponentsBuilder.fromHttpUrl(busArrive)
                .replaceQuery(String.format(
                        "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&nodeId=%s",
                        serviceKey, 1, 10, "json", busStop.getCityCode(), busStop.getBusStopId()
                ))
                .build(true)
                .toUri();

        log.info("정류장별로 도착 예정 정보 URI: {}", uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(jsonNode -> log.info("응답 데이터: {}", jsonNode.toPrettyString()))
                .map(jsonNode -> jsonNode.path("response").path("body").path("items"))
                .flatMap(jsonNode -> {
                    try {

                        if(JsonNodeUtil.exceptionEmpty(jsonNode).isPresent()){
                            return Mono.just(Collections.emptyList());
                        }
                        else{
                            List<BusArriveItemDto> itemList = objectMapper.treeToValue(jsonNode,BusArriveItem.class).getItem();
                            log.info("itemList: {}", itemList);
                            return Mono.just(itemList);
                        }
                    } catch (JsonProcessingException e) {
                        log.error("JSON 변환 실패: {}", e.getMessage(), e);
                        return Mono.error(new IllegalArgumentException("도착정보 JSON 변환 실패", e));
                    }
                });
    }

    //버스 정류장 위치
    public Optional<BusLocationItemDto> busLocationRequest(BusRequest busRequest){
        URI uri = UriComponentsBuilder.fromHttpUrl(busLocation)
                .replaceQuery(String.format(
                        "serviceKey=%s&pageNo=%d&numOfRows=%d&_type=%s&cityCode=%d&routeId=%s",
                        serviceKey, 1, 10, "json", busRequest.cityCode(), busRequest.routeId()
                ))
                .build(true)
                .toUri();

        log.info("버스 위치 정보 URI: {}", uri);

        JsonNode jsonNode = restTemplate.getForObject(uri, JsonNode.class).path("response").path("body").path("items");

        if(JsonNodeUtil.exceptionEmpty(jsonNode).isPresent()){
            return Optional.empty();
        }
        else{
            try{
                return Optional.of(objectMapper.treeToValue(jsonNode, BusLocationItem.class).getItem().stream().filter(busLocationItemDto -> busLocationItemDto.getNodeOrd() >= busRequest.busStopCount()).sorted(Comparator.comparingInt(BusLocationItemDto::getNodeOrd)).findFirst().get());
            }
            catch (JsonProcessingException e){
                log.error("NationalBusStopClient -> busLocationRequest Error : {}", e.toString());
                return Optional.empty();
            }
        }
    }

}
