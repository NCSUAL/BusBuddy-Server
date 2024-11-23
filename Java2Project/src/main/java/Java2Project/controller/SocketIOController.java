package Java2Project.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
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
            log.info("클라이언트가 연결되었습니다. {}", socketIOClient.getHandshakeData().getAddress());

        };
    }

    private DisconnectListener disconnectListener(){
        return socketIOClient -> {
            log.info("클라이언트와 연결이 종료되었습니다. {}", socketIOClient.getSessionId().toString());
            socketIOClient.disconnect();
        };
    }

    @OnEvent("busInfo")
    private void dataResponse(){

    }
}
