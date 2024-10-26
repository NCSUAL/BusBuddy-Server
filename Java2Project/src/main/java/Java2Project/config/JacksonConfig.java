package Java2Project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // List 타입에 대한 빈 문자열을 빈 리스트로 처리
        mapper.coercionConfigFor(List.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
        // Items 타입에 대한 빈 문자열을 null로 처리할 수도 있음
        mapper.coercionConfigFor(Java2Project.dto.arriveBus.Items.class)
                .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
        return mapper;
    }
}