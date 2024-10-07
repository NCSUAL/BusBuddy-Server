package Java2Project.service;

import Java2Project.domain.User;
import Java2Project.domain.UserData;
import Java2Project.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp(){
        //mockito 객체 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 내용조회(){
        //given
        User user = User
                .builder()
                .username("TeamA")
                .build();

        UserData userData =  UserData
                .builder()
                .user(user)
                .content("Test")
                .build();

        UserData userData2 =  UserData
                .builder()
                .user(user)
                .content("Test2")
                .build();

        //연관관계 설정
        user.addUserData(userData);
        user.addUserData(userData2);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        //when
        List<UserData> findContents = userService.findContents(user.getUsername());


        //then
        assertEquals(findContents.size(),2);
    }
}
