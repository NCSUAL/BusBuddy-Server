package Java2Project.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketIOController {

    public SocketIOController(SocketIOServer socketIOServer) {
        socketIOServer.addConnectListener(connectListener());
        socketIOServer.addDisconnectListener(disconnectListener());
    }

    private ConnectListener connectListener(){
        return socketIOClient -> {
            log.info("connect: {}", socketIOClient.getHandshakeData().getAddress());
        };
    }

    private DisconnectListener disconnectListener(){
        return socketIOClient -> {
            log.info("disconnect: {}", socketIOClient.getSessionId().toString());
            socketIOClient.disconnect();
        };
    }
}
