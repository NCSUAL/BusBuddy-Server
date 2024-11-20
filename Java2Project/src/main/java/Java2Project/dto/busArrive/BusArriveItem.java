package Java2Project.dto.busArrive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class BusArriveItem {
    private List<BusArriveItemDto> item;
}
