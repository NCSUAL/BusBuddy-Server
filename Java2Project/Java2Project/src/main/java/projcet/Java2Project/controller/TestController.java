package projcet.Java2Project.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test")
    public String getTest() {
        return "get 테스트";
    }

    @PostMapping("/test")
    public String postTest() {
        return "post 테스트";
    }

    @PutMapping("/test")
    public String putTest() {
        return "put 테스트";
    }

    @DeleteMapping("/test")
    public String deleteTest() {
        return "delete 테스트";
    }

    @PatchMapping("/test")
    public String patchTest() {
        return "patch 테스트";
    }
}
