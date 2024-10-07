package Java2Project.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseUser {
    private final Long id;
    private final String username;
    private final LocalDateTime created_at;

    //factory
    @Builder
    private ResponseUser(Long id, String username, LocalDateTime created_at) {
        this.id = id;
        this.username = username;
        this.created_at = created_at;
    }

    public static ResponseUser of(Long id, String username, LocalDateTime created_at){
        return ResponseUser.builder()
                .id(id)
                .username(username)
                .created_at(created_at)
                .build();
    }

}
