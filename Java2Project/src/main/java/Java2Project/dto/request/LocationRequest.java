package Java2Project.dto.request;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LocationRequest (
        @NotNull(message = "위도는 포함되어야 합니다")
        @Max(value = 90,message = "위도는 -90 ~ 90 여야 합니다")
        @Min(value = -90,message = "위도는 -90 ~ 90 여야 합니다")
        Double latitude,
        @NotNull(message = "경도는 포함되어야 합니다")
        @Max(value = 180,message = "경도는 -180 ~ 180 여야 합니다")
        @Min(value = -180,message = "경도는 -180 ~ 180 여야 합니다")
        Double longitude){}

