package Java2Project.controller;

import Java2Project.domain.User;
import Java2Project.service.UserDataService;
import Java2Project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;

import static org.mockito.Mockito.when;

//mainController 테스트 코드
@WebMvcTest(controllers = MainContoller.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDataService userDataService;


    @Test
    public void 전체유저조회() throws Exception {
        //given
        User user1 = User
                .builder()
                .username("testNameA")
                .build();

        User user2 = User
                .builder()
                .username("testNameB")
                .build();

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        //when
        mockMvc.perform(get("/api/user"))

                //then
                .andExpect(status().isOk())
        ;
    }
}
