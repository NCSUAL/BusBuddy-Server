package Java2Project.exception;

import lombok.Getter;

@Getter
public class NotFoundBusStop extends RuntimeException{
    public NotFoundBusStop(String meg) {
        super(meg);
    }
}
