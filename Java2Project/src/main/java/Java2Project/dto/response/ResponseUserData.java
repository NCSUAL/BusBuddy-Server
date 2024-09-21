package Java2Project.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseUserData {
    private String username;
    private Long userData_id;
    private String content;

    @Builder
    private ResponseUserData(String username, Long userData_id, String content) {
        this.content = content;
        this.username = username;
        this.userData_id = userData_id;
    }

    public static ResponseUserData of(String username, Long userData_id, String content) {
        return ResponseUserData.builder()
                .content(content)
                .username(username)
                .userData_id(userData_id)
                .build();
    }
}
