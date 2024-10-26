package Java2Project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    @Column(name = "city_name")
    private String cityName;

    protected BusStop(){

    }

    @Builder
    public BusStop(String stopName, BigDecimal latitude, BigDecimal longitude,String cityName,Integer cityCode, String busStopId) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.busStopId = busStopId;
        this.longitude = longitude;
        this.cityName = cityName;
        this.cityCode = cityCode;

    }
}
