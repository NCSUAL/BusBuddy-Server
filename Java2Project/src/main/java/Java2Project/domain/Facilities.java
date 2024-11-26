package Java2Project.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Facilities {
    @Id
    @GeneratedValue
    private Long FacilityId;
    private Long facilityIndex;
    private Long hasChair; // 의자 여부
    private Long hasArrivalInfoSystem; // 도착정보시스템 여부
    private Long hasCeiling; // 천장 여부
    private Long hasWiFi; // 와이파이 여부
    private Long hasHeatedChair; // 온열의자 여부
    private Long hasAirConditioning; // 에어컨 여부
    private Long hasCharger; // 충전기 여부

    @OneToOne
    @JoinColumn(name = "bus_stops_id")
    private BusStop busStop;


    @Builder
    public Facilities(Long facilityIndex, Long hasChair, Long hasArrivalInfoSystem, Long hasCeiling, Long hasWiFi, Long hasHeatedChair, Long hasAirConditioning, Long hasCharger) {
        this.facilityIndex = facilityIndex;
        this.hasChair = hasChair;
        this.hasArrivalInfoSystem = hasArrivalInfoSystem;
        this.hasCeiling = hasCeiling;
        this.hasWiFi = hasWiFi;
        this.hasHeatedChair = hasHeatedChair;
        this.hasAirConditioning = hasAirConditioning;
        this.hasCharger = hasCharger;
    }

    protected Facilities() {

    }

    //연관관계 설정
    public void setBusStop(BusStop busStop){
        this.busStop =busStop;
        busStop.setFacilities(this);
    }
}
