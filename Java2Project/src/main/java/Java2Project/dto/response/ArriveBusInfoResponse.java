package Java2Project.dto.response;

import Java2Project.dto.arriveBus.BusArriveResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArriveBusInfoResponse {
    private BusArriveResponse response;
}
