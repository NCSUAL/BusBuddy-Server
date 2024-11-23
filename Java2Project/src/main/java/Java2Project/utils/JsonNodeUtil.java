package Java2Project.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
public class JsonNodeUtil {

    public static Optional<Collection<?>> exceptionEmpty(JsonNode jsonNode){
        // jsonNode가 빈 문자열인지 확인
        if (jsonNode.isTextual() && jsonNode.asText().isEmpty()) {
            log.info("items가 빈 문자열입니다.");
            return Optional.of(Collections.emptyList());
        }

        // jsonNode가 ObjectNode가 아닌 경우 처리
        if (!(jsonNode instanceof ObjectNode || jsonNode instanceof ArrayNode)) {
            log.info("items가 ObjectNode나 ArrayNode가 아닙니다.");
            return Optional.of(Collections.emptyList());
        }

        return Optional.empty();
    }
}
