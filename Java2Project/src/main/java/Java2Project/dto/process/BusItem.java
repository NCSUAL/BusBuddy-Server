package Java2Project.dto.process;

import Java2Project.domain.BusStop;
import Java2Project.dto.busArrive.BusArriveItemDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BusItem {
    private final BusArriveItemDto busArriveItemDto;
    private final Integer cityCode;

    @Builder
    public BusItem(BusArriveItemDto busArriveItemDto, Integer cityCode) {
        this.busArriveItemDto = busArriveItemDto;
        this.cityCode = cityCode;
    }

    public static BusItem of(BusArriveItemDto busArriveItemDto, BusStop busStop){
        return BusItem.builder()
                .busArriveItemDto(busArriveItemDto)
                .cityCode(busStop.getCityCode())
                .build();
    }
}
