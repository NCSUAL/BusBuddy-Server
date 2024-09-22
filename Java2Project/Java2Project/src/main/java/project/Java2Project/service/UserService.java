package project.Java2Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.Java2Project.domain.Content;
import project.Java2Project.domain.User;
import project.Java2Project.repository.ContentRepository;
import project.Java2Project.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    @Autowired
    public UserService(UserRepository userRepository, ContentRepository contentRepository) {
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
    }

    // 등록
    @Transactional
    public void createUserData(String username, String contentText) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setUsername(username);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);

        }
        Content content = new Content();
        content.setContent(contentText);
        content.setUser(user);
        contentRepository.save(content);
    }

    // 모든 유저 조회
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 특정 유저 조회
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // 유저 이름 수정
    @Transactional
    public Optional<User> updateUsername(Long id, String newName) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(newName);
            userRepository.save(user);
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    // 유저 삭제
    @Transactional
    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // 유저 이름으로 유저 조회(내용조회할때 써먹음)
    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    // 유저 이름으로 등록된 내용 수정
    @Transactional
    public Optional<Content> updateContent(String userName, Long contentId, String newContent) {
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if (contentOptional.isPresent() && contentOptional.get().getUser().getUsername().equals(userName)) {
            Content content = contentOptional.get();
            content.setContent(newContent);
            contentRepository.save(content);
            return Optional.of(content);
        } else {
            return Optional.empty();
        }
    }

    // 유저 이름으로 등록된 내용 삭제
    @Transactional
    public boolean deleteContent(String userName, Long contentId) {
        Optional<Content> contentOptional = contentRepository.findById(contentId);
        if (contentOptional.isPresent() && contentOptional.get().getUser().getUsername().equals(userName)) {
            contentRepository.deleteById(contentId);
            return true;
        } else {
            return false;
        }
    }
}