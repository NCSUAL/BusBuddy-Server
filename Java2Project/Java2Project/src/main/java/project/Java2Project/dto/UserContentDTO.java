package project.Java2Project.dto;

public class UserContentDTO {

    private String username;
    private Long userDataId;
    private String Content;

    public UserContentDTO(String username, Long userDataId, String content) {
        this.username = username;
        this.userDataId = userDataId;
        Content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserDataId() {
        return userDataId;
    }

    public void setUserDataId(Long userDataId) {
        this.userDataId = userDataId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
