package Java2Project.service;
import Java2Project.client.NationalBusStopClient;
import Java2Project.domain.BusStop;
import Java2Project.domain.BusStopReview;
import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busRoute.BusRouteItemDto;
import Java2Project.dto.busStop.BusStopItemDto;
import Java2Project.dto.process.BusArriveDto;
import Java2Project.dto.request.LocationRequest;
import Java2Project.dto.request.ReviewRequest;
import Java2Project.dto.response.BusArriveProvideResponse;
import Java2Project.dto.response.BusStopResponse;
import Java2Project.dto.response.ReviewResponse;
import Java2Project.exception.NotFoundBusStop;
import Java2Project.repository.BusStopRepository;
import Java2Project.repository.BusStopReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;

@Service
@Slf4j
@PropertySource("classpath:uri.properties")
public class BusStopService {

    private final BusStopRepository busStopRepository;

    private final BusStopReviewRepository busStopReviewRepository;

    private final NationalBusStopClient nationalBusStopClient;

    public BusStopService(BusStopRepository busStopRepository,BusStopReviewRepository busStopReviewRepository,NationalBusStopClient nationalBusStopClient) {
        this.busStopRepository = busStopRepository;
        this.busStopReviewRepository = busStopReviewRepository;
        this.nationalBusStopClient = nationalBusStopClient;
    }

    //댓글 추가
    public ReviewResponse addComment(ReviewRequest reviewRequest){
        //BusStopId로 정류장 조회
        BusStop busStop = busStopRepository.findById(reviewRequest.busStopId())
                .stream().findFirst().orElseThrow(() -> new NotFoundBusStop("해당 버스 정류장은 없습니다."));

        return ReviewResponse.of(busStopReviewRepository.save(BusStopReview
                .builder()
                .busStop(busStop)
                .rating(reviewRequest.Rated())
                .reviewText(reviewRequest.comment())
                .build()));
    }

    //노선 정보 조회
    public Mono<List<BusRouteItemDto>> busRouteInfo(BusStop busStop){
        return nationalBusStopClient.busRouteRequest(busStop);
    }

    //공공데이터 api 요청 받고 데이터를 저장할 수 있음.
    public Mono<List<BusStop>> busStopInfo(LocationRequest locationRequest){
        return Mono.fromCallable(() -> busStopRepository.findByLatitudeAndLongitudeWithOption(
                        locationRequest.latitude(),
                        locationRequest.longitude()
                ))
                .subscribeOn(Schedulers.boundedElastic()) // 블로킹 작업을 전용 스레드에서 실행
                .flatMap(busStops -> {
                    if (busStops.isEmpty()) {
                        log.info("*** DB에 데이터 없음, API 호출 진행 ***");
                        return nationalBusStopClient.busStopLocationRequest(locationRequest)
                                .doOnNext(busStopItemDtos -> {
                                    log.info("Data: {}", busStopItemDtos);
                                    if (busStopItemDtos.isEmpty()) {
                                        throw new NotFoundBusStop("해당 범위에 버스정류장은 없습니다.");
                                    }
                                })
                                .map(busStopItemDtos -> busStopItemDtos.stream()
                                        .filter(busStopItemDto -> busStopItemDtos.get(0).getNodeno().equals(busStopItemDto.getNodeno()))
                                        .map(BusStop::of)
                                        .toList()
                                )
                                .doOnNext(filteredBusStops -> {
                                    log.info("*** 필터링된 데이터 저장 ***");
                                    busStopRepository.saveAll(filteredBusStops); // 여전히 블로킹 메서드
                                    log.info("저장 완료");
                                });
                    } else {
                        busStops.sort(Comparator.comparingDouble(busStop ->
                                Math.abs(locationRequest.latitude() - busStop.getLatitude().doubleValue()) +
                                        Math.abs(locationRequest.longitude() - busStop.getLongitude().doubleValue())
                        ));
                        return Mono.just(busStops.stream().filter(busStop -> busStops.get(0).getNodeNo().equals(busStop.getNodeNo())).toList());
                    }
                });
    }


    //정류소별 도착 예정 정보 목록 조회
    /*
    정류소별로 실시간 도착 예정 정보 및 운행정보 목록,노선 정보를 조회한다.
    */
    public Mono<BusArriveProvideResponse> arriveBusInfo(LocationRequest locationRequest) {
        log.info("-- 정류소 도착 예정 정보 비동기식으로 공공데이터에 요청 --");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        return busStopInfo(locationRequest)
                .flatMapMany(Flux::fromIterable) // List<BusStop> -> Flux<BusStop>
                .flatMap(busStop -> {
                    // 노선 및 도착 요청을 병렬로 실행
                    Mono<List<BusRouteItemDto>> routeRequest = nationalBusStopClient.busRouteRequest(busStop)
                            .subscribeOn(Schedulers.boundedElastic()); // 병렬 실행
                    Mono<List<BusArriveItemDto>> arriveRequest = nationalBusStopClient.busArriveRequest(busStop)
                            .subscribeOn(Schedulers.boundedElastic()); // 병렬 실행

                    return Mono.zip(Mono.just(busStop), routeRequest, arriveRequest); // BusStop 포함한 병합된 결과 반환
                })
                .collectList() // 모든 결과를 List<Tuple3<...>>로 수집
                .map(results -> {
                    List<BusArriveDto> arriveBusDtos = new ArrayList<>();
                    List<BusRouteItemDto> busRouteItemDtos = new ArrayList<>();
                    List<BusArriveItemDto> items = new ArrayList<>();
                    List<BusStopResponse> busStopResponses = new ArrayList<>();

                    // 각 요청 결과를 분류
                    results.forEach(result -> {
                        BusStop busStop = result.getT1();
                        busRouteItemDtos.addAll(result.getT2());
                        items.addAll(result.getT3());

                        // BusStop 정보를 BusStopResponse로 변환하여 추가
                        busStopResponses.add(BusStopResponse.of(busStop));
                    });

                    // 도착 정보와 노선 정보를 매핑
                    for (BusArriveItemDto busArriveItemDto : items) {
                        for (BusRouteItemDto busRouteItemDto : busRouteItemDtos) {
                            if (busArriveItemDto.getRouteid().equals(busRouteItemDto.getRouteid())) {
                                arriveBusDtos.add(BusArriveDto.of(busArriveItemDto, busRouteItemDto));
                            }
                        }
                    }

                    stopWatch.stop();
                    log.info("총 처리 시간: {} ms", stopWatch.getTotalTimeMillis());

                    // 최종 응답 생성
                    return BusArriveProvideResponse.builder()
                            .busStopResponse(busStopResponses) // 업데이트된 리스트 사용
                            .items(arriveBusDtos)
                            .build();
                });
    }
}