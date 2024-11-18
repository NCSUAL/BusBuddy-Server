package Java2Project.dto.process;

import Java2Project.dto.arriveBus.ArriveBusItemDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArriveBusDto {
    private String busStopCount; //도착지까지 남은 정류장 수
    private String nodenm;
    private String arriveTime;   //도착까지 남은 시간(초)
    private String busNumber;   //버스 노선 번호

    @Builder
    private ArriveBusDto(String busStopCount, String arriveTime, String busNumber,String nodenm) {
        this.arriveTime = arriveTime;
        this.nodenm = nodenm;
        this.busStopCount = busStopCount;
        this.busNumber = busNumber;
    }

    //factory
    public static ArriveBusDto of(ArriveBusItemDto arriveBusItemDto){
        //도착 시간을 가져옴
        int arrtime = arriveBusItemDto.getArrtime();
        int seconds = arrtime % 60;
        int minutes = arrtime / 60 + (seconds>=40? 1: 0);

        String arriveTime = minutes<=2? "곧 도착" : minutes +"분";

        return ArriveBusDto
                .builder()
                .busNumber(arriveBusItemDto.getRouteno())
                .nodenm(arriveBusItemDto.getNodenm())
                .arriveTime(arriveTime)
                .busStopCount(arriveBusItemDto.getArrprevstationcnt().toString())
                .build();
    }


}
