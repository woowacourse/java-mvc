package nextstep.mvc.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JsonParserUtils {

    private JsonParserUtils() {
    }

    public static String toJson(Map<String, Object> model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
    }
}
