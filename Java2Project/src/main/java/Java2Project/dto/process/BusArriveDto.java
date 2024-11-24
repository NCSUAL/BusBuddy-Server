package Java2Project.dto.process;

import Java2Project.dto.busArrive.BusArriveItemDto;
import Java2Project.dto.busRoute.BusRouteItemDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BusArriveDto {
    private final String busStopCount; //도착지까지 남은 정류장 수
    private final String nodenm;       //현재 버스가 위치한 정류장
    private final String arriveTime;   //도착까지 남은 시간(초)
    private final String busNumber;    //버스 노선 번호
    private final Integer cityCode;       //관리 도시
    private final String routeInfo;    //버스 노선 정보
    private final String routeId;      //버스 노선 ID

    @Builder
    private BusArriveDto(String busStopCount, String arriveTime, String busNumber, String nodenm, String routeInfo,String routeId,Integer cityCode) {
        this.arriveTime = arriveTime;
        this.nodenm = nodenm;
        this.busStopCount = busStopCount;
        this.busNumber = busNumber;
        this.routeInfo = routeInfo;
        this.routeId = routeId;
        this.cityCode = cityCode;
    }

    //factory
    public static BusArriveDto of(BusItem busItem, BusRouteItemDto busRouteItemDto){
        //도착 시간을 가져옴
        int arrtime = busItem.getBusArriveItemDto().getArrtime();
        int minutes = arrtime / 60;

        String arriveTime = minutes<2? "곧 도착" : minutes +"분";

        return BusArriveDto
                .builder()
                .busNumber(busItem.getBusArriveItemDto().getRouteno())
                .nodenm(busItem.getBusArriveItemDto().getNodenm())
                .arriveTime(arriveTime)
                .routeInfo(busRouteItemDto.getStartnodenm().concat(" ↔ ").concat(busRouteItemDto.getEndnodenm()))
                .busStopCount(busItem.getBusArriveItemDto().getArrprevstationcnt().toString())
                .routeId(busRouteItemDto.getRouteid())
                .cityCode(busItem.getCityCode())
                .build();
    }


}
