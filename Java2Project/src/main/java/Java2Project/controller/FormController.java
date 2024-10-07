package Java2Project.controller;


import Java2Project.domain.User;
import Java2Project.domain.UserData;
import Java2Project.service.UserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@Slf4j
public class FormController {

    @Autowired
    private UserDataService userDataService;

    @PostMapping("/submit")
    public String formDispose(String username, String content){

        User user = User.builder()
                .username(username)
                .build();

        UserData userData = UserData.builder()
                                    .content(content)
                                    .user(user)
                                    .build();

        user.addUserData(userData);

        userDataService.saveUserData(user, userData);
        return "redirect:/";
    }
}
