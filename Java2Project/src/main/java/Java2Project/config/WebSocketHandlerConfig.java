package Java2Project.config;

import Java2Project.client.NationalBusStopClient;
import Java2Project.handler.WebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketHandlerConfig {

    private final NationalBusStopClient nationalBusStopClient;

    public WebSocketHandlerConfig(NationalBusStopClient nationalBusStopClient) {
        this.nationalBusStopClient = nationalBusStopClient;
    }


    @Bean
    public org.springframework.web.socket.WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(nationalBusStopClient);
    }
}
