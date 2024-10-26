package Java2Project.controller.exception;

import Java2Project.exception.NotFoundBusStop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class BusStopExceptionHandler {


    //버스정류장을 찾지 못했을 때
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundBusStop.class)
    public ResponseEntity<?> notFoundBusStopException(NotFoundBusStop e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //body 데이터 검증
    //데이터가 null 임.
    //위도, 경도 범위가 적절하지 않음.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class,})
    public ResponseEntity<Map<String,String>> locationNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for(ObjectError error : allErrors){
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



}
