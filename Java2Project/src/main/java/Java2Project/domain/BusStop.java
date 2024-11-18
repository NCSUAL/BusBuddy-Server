package Java2Project.domain;

import Java2Project.dto.busStop.BusStopItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bus_stops")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BusStop {

    @Id
    @Column(name = "bus_stops_id")
    private String busStopId;

    @Column(name = "stop_name")
    private String stopName;

    @Column(precision = 11,scale = 8)
    private BigDecimal latitude; //위도

    @Column(precision = 11,scale = 8)
    private BigDecimal longitude; //경도

    @Column(name =  "city_code")
    private Integer cityCode;

    //cascade: DB 개념, 부모에서 데이터가 변경되면 자식에도 영향을 준다
    //orpanRemoval: DB 개념, 부모와 자식이 끊어지면 자식 전체를 삭제한다.
    @OneToMany(mappedBy = "stopId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<BusStopReview> busStopReviews = new ArrayList<>();

    protected BusStop(){

    }

    @Builder
    private BusStop(String stopName, BigDecimal latitude, BigDecimal longitude,Integer cityCode, String busStopId) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.busStopId = busStopId;
        this.longitude = longitude;
        this.cityCode = cityCode;
    }

    public static BusStop of(BusStopItemDto busStopItemDto){
        return BusStop.builder()
                .busStopId(busStopItemDto.getNodeid())
                .stopName(busStopItemDto.getNodenm())
                .latitude(BigDecimal.valueOf(busStopItemDto.getGpslati()))
                .longitude(BigDecimal.valueOf(busStopItemDto.getGpslong()))
                .cityCode(busStopItemDto.getCitycode())
                .build();
    }

    //연관관계 설정 시 파장을 일으킬 수 있음.
    @Override
    public String toString(){
        return "busStopId: " + busStopId + ", stopName: " + stopName + ", latitude: " + latitude+ ", longitude: " + longitude+ ", cityCode: " + cityCode;
    }
}
