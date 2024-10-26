package Java2Project.dto.arriveBus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemDto {
    private Integer arrprevstationcnt; //도착지까지 남은 정류장 수
    private Integer arrtime;          //도착까지 남은 시간(초)
    private String nodeid;            //정류장 고유 ID
    private String nodenm;            //정류장 이름
    private String routeid;           //버스 노선 ID
    private Integer routeno;           //버스 노선 번호

    //버스 유형
    /*
    지선버스 -> 마을버스를 의미
     */
    private String routetp;

    //차량 유형
    /*
    저상버스: 저상버스를 의미
    일반차량: 일반 버스를 의미
     */
    private String vehicletp;
}
