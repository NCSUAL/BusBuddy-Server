package Java2Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder webClient) {
        return
                webClient
                        .baseUrl("https://apis.data.go.kr/")
                        .build();
    }
}
