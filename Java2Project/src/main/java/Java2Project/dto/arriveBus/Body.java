package Java2Project.dto.arriveBus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Setter
@ToString
public class Body {
    private ArriveBusItems items;
    @Getter
    private int numOfRows;
    @Getter
    private int pageNo;
    @Getter
    private int totalCount;

    public ArriveBusItems getItems() {
        if(items == null){
            items = new ArriveBusItems();
            items.setItem(new ArrayList<>());
        }
        return items;
    }

}
