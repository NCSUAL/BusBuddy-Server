package Java2Project.configuration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource("classpath:uri.properties")
public class UriConfiguration {

    @Value("${exampleURI}")
    private String uri;

    @Test
    public void getUri(){
        System.out.println(uri);
        assertNotNull(uri);
    }
}
