package Java2Project;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketIOLifeCycle {
    private static final Logger log = LoggerFactory.getLogger(SocketIOLifeCycle.class);
    private final SocketIOServer server;
    public SocketIOLifeCycle(SocketIOServer server) {
        this.server = server;
    }

    //애플리케이션 실행 전 실행
    @PostConstruct
    void start(){
        try{
            server.start();
        }
        catch (Exception e){
            log.error("SocketIOLifeCyCle server 실행 오류: ", e);
        }
    }


    //애플리케이션 종료 전 종료
    @PreDestroy
    void stop(){
        try{
            server.stop();
        }
        catch (Exception e){
            log.error("SocketIOLifeCyCle server 종료 오류: ", e);
        }
    }
}
