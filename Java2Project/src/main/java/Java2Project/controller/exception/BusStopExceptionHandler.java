package Java2Project.controller.exception;

import Java2Project.exception.NotFoundBusStop;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusStopExceptionHandler {

    //버스정류장을 찾지 못했을 때
    @ExceptionHandler(NotFoundBusStop.class)
    public ResponseEntity<?> notFoundBusStopException(NotFoundBusStop e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
