package Java2Project.dto.response;
import Java2Project.dto.process.ArriveBusDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ArriveBusProvideResponse {
    private List<BusStopResponse> busStopResponse;
    private List<ArriveBusDto> items;

    @Builder
    public ArriveBusProvideResponse(List<BusStopResponse> busStopResponse, List<ArriveBusDto> items) {
        this.busStopResponse = busStopResponse;
        this.items = items;
    }
}
