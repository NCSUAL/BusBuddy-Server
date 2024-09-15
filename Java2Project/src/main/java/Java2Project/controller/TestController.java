package Java2Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestController {

    @GetMapping("getTest")
    public String getTest(){
        return "index";
    }

    @PostMapping("postTest")
    @ResponseBody
    public String postTest(){
        return "postTest";
    }

    @PatchMapping("patchTest")
    @ResponseBody
    public String patchTest(){
        return "patchTest";
    }

    @PutMapping("putTest")
    @ResponseBody
    public String putTest(){
        return "putTest";
    }

    @DeleteMapping("deleteTest")
    @ResponseBody
    public String deleteTest(){
        return "deleteTest";
    }
}
