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
    @GeneratedValue
    @Column(name = "id")
    private Long busStopId;

    @Column(name = "stop_name")
    private String stopName;

    @Column(precision = 10,scale = 8)
    private BigDecimal latitude; //위도

    @Column(precision = 11,scale = 8)
    private BigDecimal longitude; //경도

    @Column(name = "city_name")
    private String cityName;

    protected BusStop(){

    }

    @Builder
    public BusStop(String stopName, BigDecimal latitude, BigDecimal longitude,String cityName) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
    }
}
