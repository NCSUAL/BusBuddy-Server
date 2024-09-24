package project.Java2Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserContentDTO {

    private String username;
    private Long userDataId;
    private String content;
}
