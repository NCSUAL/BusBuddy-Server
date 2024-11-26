package Java2Project.dto.response;

import Java2Project.domain.Facilities;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FacilitiesResponse {
    private boolean hasChair; // 의자 여부
    private boolean hasArrivalInfoSystem; // 도착정보시스템 여부
    private boolean hasCeiling; // 천장 여부
    private boolean hasWiFi; // 와이파이 여부
    private boolean hasHeatedChair; // 온열의자 여부
    private boolean hasAirConditioning; // 에어컨 여부
    private boolean hasCharger; // 충전기 여부

    @Builder
    public FacilitiesResponse(boolean hasChair, boolean hasArrivalInfoSystem, boolean hasCeiling, boolean hasWiFi, boolean hasHeatedChair, boolean hasAirConditioning, boolean hasCharger) {
        this.hasChair = hasChair;
        this.hasArrivalInfoSystem = hasArrivalInfoSystem;
        this.hasCeiling = hasCeiling;
        this.hasWiFi = hasWiFi;
        this.hasHeatedChair = hasHeatedChair;
        this.hasAirConditioning = hasAirConditioning;
        this.hasCharger = hasCharger;
    }

    public static FacilitiesResponse of(Facilities facilities) {
        return FacilitiesResponse.builder()
                .hasAirConditioning(((double) facilities.getHasAirConditioning() / facilities.getFacilityIndex()) >=0.5)
                .hasChair(((double) facilities.getHasChair() / facilities.getFacilityIndex()) >=0.5)
                .hasCharger(((double) facilities.getHasCharger() / facilities.getFacilityIndex()) >=0.5)
                .hasCeiling(((double) facilities.getHasCeiling() / facilities.getFacilityIndex()) >=0.5)
                .hasWiFi(((double) facilities.getHasWiFi() / facilities.getFacilityIndex()) >=0.5)
                .hasHeatedChair(((double) facilities.getHasHeatedChair() / facilities.getFacilityIndex()) >=0.5)
                .hasArrivalInfoSystem(((double) facilities.getHasArrivalInfoSystem() / facilities.getFacilityIndex()) >=0.5)
                .build();
    }

}
