package Java2Project.dto.response;

import Java2Project.domain.BusStop;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class BusStopResponse {
    private Long id;
    private String stopName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String cityName;

    @Builder
    private BusStopResponse(Long id, String stopName, BigDecimal latitude, BigDecimal longitude,String cityName){
        this.id = id;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
    }


    //factory
    public static BusStopResponse of(Long id, String stopName, BigDecimal latitude, BigDecimal longitude,String cityName){
        return BusStopResponse.builder()
                .id(id)
                .stopName(stopName)
                .latitude(latitude)
                .longitude(longitude)
                .cityName(cityName)
                .build();
    }

    public static BusStopResponse of(BusStop busStop){
        return BusStopResponse.builder()
                .id(busStop.getBusStopId())
                .stopName(busStop.getStopName())
                .latitude(busStop.getLatitude())
                .longitude(busStop.getLongitude())
                .cityName(busStop.getCityName())
                .build();
    }

}
