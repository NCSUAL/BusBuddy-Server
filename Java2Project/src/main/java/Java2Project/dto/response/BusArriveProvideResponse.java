package Java2Project.dto.response;
import Java2Project.dto.process.BusArriveDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BusArriveProvideResponse {
    private List<BusStopResponse> busStopResponse;
    private List<BusArriveDto> items;

    @Builder
    public BusArriveProvideResponse(List<BusStopResponse> busStopResponse, List<BusArriveDto> items) {
        this.busStopResponse = busStopResponse;
        this.items = items;
    }
}
