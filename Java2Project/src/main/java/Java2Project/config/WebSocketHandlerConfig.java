package Java2Project.config;

import Java2Project.client.NationalBusStopClient;
import Java2Project.handler.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;

@Configuration
public class WebSocketHandlerConfig {

    private final NationalBusStopClient nationalBusStopClient;

    public WebSocketHandlerConfig(NationalBusStopClient nationalBusStopClient) {
        this.nationalBusStopClient = nationalBusStopClient;
    }


    @Bean
    public WebSocketHandler webSocketHandler() {
        return new MyWebSocketHandler(nationalBusStopClient);
    }
}
