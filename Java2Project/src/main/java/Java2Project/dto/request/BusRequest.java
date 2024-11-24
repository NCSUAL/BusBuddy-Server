package Java2Project.dto.request;

import jakarta.validation.constraints.NotNull;

public record BusRequest (
        @NotNull(message = "arriveTime은 포함되어야 합니다.") String arriveTime,
        @NotNull(message = "busStopCount는 포함되어야 합니다") Integer busStopCount,
        @NotNull(message = "cityCode는 포함되어야 합니다.") Integer cityCode,
        @NotNull(message = "routeId는 포함되어야 합니다.") String routeId,
        @NotNull(message = "busNumber는 포함되어야 합니다.") String busNumber
){}

