package Java2Project.dto.web;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Items {
    private List<ItemDto> item;
}
