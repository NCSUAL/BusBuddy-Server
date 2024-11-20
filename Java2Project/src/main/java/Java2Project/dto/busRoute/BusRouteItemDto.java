package Java2Project.dto.busRoute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusRouteItemDto {
    //버스 노선의 고유 식별자
    private String routeid;

    //버스 노선의 종점 이름
    private String endnodenm;

    //버스 노선의 번호
    private String routeno;

    //버스 노선 유형
    private String routetp;

    //버스 노선의 출발 정류소 이름
    private String startnodenm;

}
