package Java2Project.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    private final String hostName;
    private final Integer port;

    public SocketIOConfig(
            @Value("${socketIO.server.hostName}") String hostName,
            @Value("${socketIO.server.port}") Integer port) {
        this.hostName = hostName;
        this.port = port;
    }

    @Bean
    public SocketIOServer socketIOServer(){
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostName);
        config.setPort(port);
        return new SocketIOServer(config);
    }
}
