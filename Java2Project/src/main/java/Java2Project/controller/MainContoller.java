package Java2Project.controller;
import Java2Project.domain.UserData;
import Java2Project.dto.request.RequestRemoveUserData;
import Java2Project.dto.request.RequestUser;
import Java2Project.dto.request.RequestUserData;
import Java2Project.dto.response.ResponseUserData;
import Java2Project.dto.response.ResponseUser;
import Java2Project.service.UserDataService;
import Java2Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RequestMapping("/api")
@RestController
public class MainContoller{

    @Autowired
    private UserService userService;

    @Autowired
    private UserDataService userDataService;

    //전체 유저 조회
    @GetMapping("/user")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        List<ResponseUser> responseUsers = userService.findAll()
                .stream()
                .map(user -> ResponseUser.of(user.getId(),user.getUsername(),user.getCreatedAt())
                ).toList();

        return ResponseEntity.ok(responseUsers);
    }

    //특정 유저 조회
    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable Long id){
          return userService.findById(id)
                  .stream()
                  .map(user -> ResponseEntity.ok(ResponseUser.of(user.getId(), user.getUsername(), user.getCreatedAt()))
                  )
                  .findFirst()
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //특정 유저 이름 수정
    @PostMapping("/user")
    public ResponseEntity<ResponseUser> postUser(@RequestBody RequestUser requestUser){
        return userService.ModifyUsername(requestUser)
                .stream()
                .map(user -> ResponseEntity.ok(ResponseUser.of(user.getId(),user.getUsername(),user.getCreatedAt())))
                .findFirst()
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //특정 유저 삭제
    @DeleteMapping("/user/{id}")
    public ResponseEntity<List<ResponseUser>> deleteUser(@PathVariable Long id){
        List<ResponseUser> responseUsers = userService.deleteById(id)
                .stream()
                .map(user -> ResponseUser.of(user.getId(), user.getUsername(), user.getCreatedAt()))
                .toList();

        return ResponseEntity.ok(responseUsers);
    }

    //------------------------------------------------------------------------------------------
    //특정 사용자 이름으로 등록된 내용들 조회
    @GetMapping("/content/{username}")
    public ResponseEntity<List<ResponseUserData>> getContents(@PathVariable String username){
        List<ResponseUserData> responseUserData = userService.findContents(username)
                .stream()
                .map(userData -> ResponseUserData.of(username,userData.getId(),userData.getContent()))
                .toList();

        return ResponseEntity.ok(responseUserData);
    }

    //특정 사용자 이름으로 등록된 내용 수정
    @PatchMapping("/content")
    public ResponseEntity<ResponseUserData> postContent(@RequestBody RequestUserData requestUserData){
        return userDataService.ModifyContent(requestUserData)
                .stream()
                .map(userData -> ResponseEntity.ok( ResponseUserData.of(userData.getUser().getUsername(),userData.getId(),userData.getContent())))
                .findAny()
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    //특정 사용자 이름으로 등록된 내용 삭제
    @DeleteMapping("/content")
    public ResponseEntity<List<ResponseUserData>> deleteContent(@RequestBody RequestRemoveUserData requestRemoveUserData){
        List<ResponseUserData> responseUserDatas = userDataService.removeContent(requestRemoveUserData)
                .stream()
                .map(userData -> ResponseUserData.of(userData.getUser().getUsername(),userData.getId(),userData.getContent()))
                .toList();

        return ResponseEntity.ok(responseUserDatas);
    }
}
