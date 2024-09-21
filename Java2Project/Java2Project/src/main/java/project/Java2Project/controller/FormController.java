package project.Java2Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.Java2Project.service.UserService;

@Controller
@RequestMapping("/api")
public class FormController {
    private final UserService userService;

    @Autowired
    public FormController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/submit")
    public String create(@RequestParam("username") String username, @RequestParam("content") String content) {
        userService.createUserData(username, content);
        return "redirect:/";
    }
}
