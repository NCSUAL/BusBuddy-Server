package project.Java2Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.Java2Project.domain.Content;
import project.Java2Project.domain.User;
import project.Java2Project.dto.UserContentDTO;
import project.Java2Project.repository.UserRepository;
import project.Java2Project.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class ApiController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ApiController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 모든 유저 조회
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 특정 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 유저 이름 수정
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserName(@PathVariable Long id, @RequestParam String newName) {
        Optional<User> updateUser = userService.updateUsername(id, newName);
        return updateUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 유저 이름으로 내용 조회
    @GetMapping("/name/{userName}")
    public ResponseEntity<List<UserContentDTO>> getUserContents(@PathVariable String userName) {
        Optional<User> userOptional = userService.findByUsername(userName);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Content> contents = user.getContents();

            List<UserContentDTO> userContentDTOs = contents.stream()
                    .map(content -> new UserContentDTO(user.getUsername(), content.getId(), content.getContent()))
                    .toList();
            return ResponseEntity.ok(userContentDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 내용 수정
    @PutMapping("/name/{userName}/contents/{contentId}")
    public ResponseEntity<Content> updateUserContent(@PathVariable String userName, @PathVariable Long contentId, @RequestParam String newContent) {
        Optional<Content> updatedContent = userService.updateContent(userName, contentId, newContent);
        return updatedContent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 내용 삭제
    @DeleteMapping("/name/{userName}/contents/{contentId}")
    public ResponseEntity<Void> deleteContent(@PathVariable String userName, @PathVariable Long contentId) {
        boolean isDeleted = userService.deleteContent(userName, contentId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
