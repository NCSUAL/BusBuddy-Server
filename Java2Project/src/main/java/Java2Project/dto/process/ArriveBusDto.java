package Java2Project.dto.process;

import Java2Project.dto.arriveBus.ItemDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ArriveBusDto {
    private String busStopCount; //도착지까지 남은 정류장 수
    private String arriveTime;   //도착까지 남은 시간(초)
    private Integer busNumber;   //버스 노선 번호

    @Builder
    private ArriveBusDto(String busStopCount, String arriveTime, Integer busNumber) {
        this.arriveTime = arriveTime;
        this.busStopCount = busStopCount;
        this.busNumber = busNumber;
    }

    //factory
    public static ArriveBusDto of(ItemDto itemDto){
        //도착 시간을 가져옴
        int arrtime = itemDto.getArrtime();
        int minutes = arrtime / 60;
        int seconds = arrtime % 60;

        String arriveTime = minutes<1? "곧 도착" : minutes +" 분 "+ seconds+ " 초";

        return ArriveBusDto
                .builder()
                .busNumber(itemDto.getRouteno())
                .arriveTime(arriveTime)
                .busStopCount(itemDto.getArrprevstationcnt().toString())
                .build();
    }


}
