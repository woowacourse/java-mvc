package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JsonWriterObjectMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeJson(final Map<String, ?> objects) {
        try {
            return objectMapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 객체 변환 과정에서 예외가 발생했습니다.");
        }
    }
}
