package Java2Project.dto.process;

import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busRoute.BusRouteItemDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BusArriveDto {
    private String busStopCount; //도착지까지 남은 정류장 수
    private String nodenm;       //현재 버스가 위치한 정류장
    private String arriveTime;   //도착까지 남은 시간(초)
    private String busNumber;    //버스 노선 번호
    private String routeInfo;    //버스 노선 정보

    @Builder
    private BusArriveDto(String busStopCount, String arriveTime, String busNumber, String nodenm, String routeInfo) {
        this.arriveTime = arriveTime;
        this.nodenm = nodenm;
        this.busStopCount = busStopCount;
        this.busNumber = busNumber;
        this.routeInfo = routeInfo;
    }

    //factory
    public static BusArriveDto of(BusArriveItemDto busArriveItemDto, BusRouteItemDto busRouteItemDto){
        //도착 시간을 가져옴
        int arrtime = busArriveItemDto.getArrtime();
        int minutes = arrtime / 60;

        String arriveTime = minutes<2? "곧 도착" : minutes +"분";

        return BusArriveDto
                .builder()
                .busNumber(busArriveItemDto.getRouteno())
                .nodenm(busArriveItemDto.getNodenm())
                .arriveTime(arriveTime)
                .routeInfo(busRouteItemDto.getStartnodenm().concat(" ↔ ").concat(busRouteItemDto.getEndnodenm()))
                .busStopCount(busArriveItemDto.getArrprevstationcnt().toString())
                .build();
    }


}
