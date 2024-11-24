package Java2Project.dto.busLocation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusLocationItemDto {

    @JsonProperty("gpslati")
    private double gpsLati;

    @JsonProperty("gpslong")
    private double gpsLong;

    @JsonProperty("nodeid")
    private String nodeId;

    @JsonProperty("nodenm")
    private String nodeNm;

    @JsonProperty("nodeord")
    private int nodeOrd;

    @JsonProperty("routenm")
    private String routeNm;

    @JsonProperty("routetp")
    private String routeTp;

    @JsonProperty("vehicleno")
    private String vehicleNo;

}
