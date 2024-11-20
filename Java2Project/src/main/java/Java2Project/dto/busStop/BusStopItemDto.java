package Java2Project.dto.busStop;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BusStopItemDto {
    private Integer citycode;
    private double gpslati;
    private double gpslong;
    private String nodeid;
    private String nodenm;
    private Integer nodeno;
}
