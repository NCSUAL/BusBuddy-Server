package Java2Project.dto.web;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Body {
    private Items items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
