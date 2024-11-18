package Java2Project.configuration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UriConfigurationTest {

    @Value("${busArrive}")
    private String uri;

    @Test
    public void testUri(){
        assertNotNull(uri);
    }
}
