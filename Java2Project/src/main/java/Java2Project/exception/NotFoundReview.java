package Java2Project.exception;

import lombok.Getter;

@Getter
public class NotFoundReview extends RuntimeException{
    public NotFoundReview(String error){
        super(error);
    }
}
