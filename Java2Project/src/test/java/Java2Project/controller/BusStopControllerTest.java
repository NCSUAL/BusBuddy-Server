package Java2Project.controller;

import Java2Project.service.BusStopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class BusStopControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BusStopController busStopController;

    @Mock
    private BusStopService busStopService;

    @BeforeEach
    public void setUp() {
        //injection
        mockMvc = MockMvcBuilders.standaloneSetup(BusStopController.class).build();
    }

    @Test
    public void 위도경도로버스정류장찾기(){
        //given

        //when

        //then
    }


}
