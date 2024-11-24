package Java2Project.handler;

import Java2Project.client.NationalBusStopClient;
import Java2Project.dto.request.BusRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 세션별 카운터를 관리하는 맵
    private final ConcurrentHashMap<WebSocketSession, Integer> sessionCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, BusRequest> sessionData = new ConcurrentHashMap<>();

    private final NationalBusStopClient nationalBusStopClient;

    public MyWebSocketHandler(NationalBusStopClient nationalBusStopClient) {
        this.nationalBusStopClient = nationalBusStopClient;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        BusRequest busRequest = objectMapper.readValue(message.getPayload(), BusRequest.class);
        log.info("{}",busRequest.toString());
        int value = busRequest.arriveTime().equals("곧 도착")? 1 : Integer.parseInt(String.join("",busRequest.arriveTime().split("분")));
        sessionCounters.put(session,(value * 60) /5 );
        sessionData.put(session,busRequest);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("Connection closed: " + session.getId());
    }

    @Scheduled(fixedRate = 5000)
    public void sendData(){
        sessionCounters.forEach((session, integer) ->
        {
            if(session.isOpen()){
                if(sessionCounters.get(session) > 0){
                    try{
                        log.info("{} 번 남음",sessionCounters.get(session));
                        String message = objectMapper.writeValueAsString(nationalBusStopClient.busLocationRequest(sessionData.get(session)).get());
                        session.sendMessage(new TextMessage(message));
                        sessionCounters.put(session,sessionCounters.get(session) -1);
                    }
                    catch (Exception e){
                        log.error("MyWebSocketHandler -> sendData -> json 변환 실패: {}", e.toString());
                        try {
                            session.close();
                        }
                        catch (IOException e1){
                            log.error("MyWebSocketHandler Error: {}", e1.toString());
                        }
                    }
                }
            }
        });
    }
}
