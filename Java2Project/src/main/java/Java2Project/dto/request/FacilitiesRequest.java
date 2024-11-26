package Java2Project.dto.request;
import jakarta.validation.constraints.NotNull;

public record FacilitiesRequest (
        @NotNull(message = "hasChair는 포함되어야 합니다")
        boolean hasChair,
        @NotNull(message = "hasArrivalInfoSystem는 포함되어야 합니다")
        boolean hasArrivalInfoSystem,
        @NotNull(message = "hasWiFi는 포함되어야 합니다")
        boolean hasWiFi,
        @NotNull(message = "hasHeatedChair는 포함되어야 합니다")
        boolean hasHeatedChair,
        @NotNull(message = "hasAirConditioning는 포함되어야 합니다")
        boolean hasAirConditioning,
        @NotNull(message = "hasCeiling는 포함되어야 합니다")
        boolean hasCeiling,
        @NotNull(message = " hasCharger는 포함되어야 합니다")
        boolean hasCharger
){}
