package Java2Project.service;
import Java2Project.client.NationalBusStopClient;
import Java2Project.domain.BusStop;
import Java2Project.domain.BusStopReview;
import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busRoute.BusRouteItemDto;
import Java2Project.dto.busStop.BusStopItemDto;
import Java2Project.dto.process.ArriveBusDto;
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
import reactor.core.publisher.Mono;

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
    public List<BusStop> findByLocation(LocationRequest locationRequest){
        //위도 경도로 버스 정류장의 정보를 가져옴
        log.info("*** DB에서 데이터 조회 ***");
        List<BusStop> busStop = busStopRepository.findByLatitudeAndLongitudeWithOption(locationRequest.latitude(), locationRequest.longitude());

        if(busStop.isEmpty()){
            log.info("----- DB에 존재하지 않음 -----");
            log.info("*** 정류장을 api로 요청 ***");

                List<BusStopItemDto> busStopLocationRequest = nationalBusStopClient.busStopLocationRequest(locationRequest);

                log.info("Data: {}",busStopLocationRequest);

                if(busStopLocationRequest.isEmpty()){
                    throw new NotFoundBusStop("해당 범위에 버스정류장은 없습니다.");
                }

                log.info("* 데이터 필터링 진행 *");
                //list의 첫값과 nodeno가 같은 원소를 필터링하고 리턴한다
                List<BusStop> busStops =  busStopLocationRequest
                        .stream()
                        .filter(busStopItemDto -> busStopLocationRequest.get(0).getNodeno() == busStopItemDto.getNodeno())
                        .map(BusStop::of)
                        .toList();

                log.info("* 데이터 필터링 성공: {}", busStops);

                log.info("*** 데이터 저장 ***");
                busStopRepository.saveAll(busStops);

                log.info("모든 작업이 정상적으로 처리 됨");
                return busStops;
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
            items.addAll(nationalBusStopClient.busArriveRequest(busStop1));

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
                if(busArriveItemDto.getRouteid().equals(busRouteItemDto.getRouteid())){
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
