package Java2Project.handler;

import Java2Project.exception.JsonProcessing;
import Java2Project.exception.NotFoundBusStop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

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

    //body 데이터 검증 @valid
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String,String>> notValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for(ObjectError error : allErrors){
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //외부 api 요청 실패
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler({RestClientException.class})
    public ResponseEntity<?> failedApiRequestException(RestClientException ex){
        return ResponseEntity.badRequest().body("api 요청을 실패하였습니다.");
    }


    //json 변환 실패
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({JsonProcessing.class})
    public ResponseEntity<?> jsonProcessingException(JsonProcessing e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
