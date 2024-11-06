package Java2Project.dto.response;

import Java2Project.domain.BusStop;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BusStopResponse {
    private String busStopId;
    private String stopName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer cityCode;

    @Builder
    private BusStopResponse( String stopName, BigDecimal latitude, BigDecimal longitude,String busStopId,Integer cityCode) {
        this.busStopId = busStopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityCode = cityCode;
    }

    //factory
    public static BusStopResponse of( String stopName, BigDecimal latitude, BigDecimal longitude,String busStopId,Integer cityCode){
        return BusStopResponse.builder()
                .stopName(stopName)
                .busStopId(busStopId)
                .latitude(latitude)
                .longitude(longitude)
                .cityCode(cityCode)
                .build();
    }

    public static BusStopResponse of(BusStop busStop){
        return BusStopResponse.builder()
                .stopName(busStop.getStopName())
                .busStopId(busStop.getBusStopId())
                .latitude(busStop.getLatitude())
                .longitude(busStop.getLongitude())
                .cityCode(busStop.getCityCode())
                .build();
    }

}
