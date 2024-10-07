package Java2Project.service;


import Java2Project.domain.User;
import Java2Project.domain.UserData;
import Java2Project.dto.request.RequestUser;
import Java2Project.repository.UserDataRepository;
import Java2Project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }


    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<UserData> findContents(String username){
        User user = userRepository.findByUsername(username).get();
        return user.getUserdatas();
    }

    @Transactional
    public Optional<User> ModifyUsername(RequestUser requestUser){
        Optional<User> user = userRepository.findById(requestUser.id());

        if(user.isPresent()){
            user.get().setUsername(requestUser.username());
            userRepository.save(user.get());
            return user;
        }
        else{
            return Optional.empty();
        }
    }


    @Transactional
    public List<User> deleteById(Long id){
        try {
            userRepository.deleteById(id);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return userRepository.findAll();
    }

}
