package Java2Project.configuration;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:uritest.properties")
public class UriConfigurationTest {

    @Value("${arriveBusStop}")
    private String uri;

    @Test
    public void testUri(){
        assertNotNull(uri);
    }
}
