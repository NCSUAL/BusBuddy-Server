package Java2Project.domain;

import Java2Project.dto.busStop.BusStopItemDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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
