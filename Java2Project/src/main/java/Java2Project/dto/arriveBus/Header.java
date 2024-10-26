package Java2Project.dto.arriveBus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Header {
    // API 응답 코드
    /*
        00은 정상 응답
        그 외의 응답은 부정 응답
     */
    private String resultCode;

    //응답 메시지
    private String resultMsg;
}
