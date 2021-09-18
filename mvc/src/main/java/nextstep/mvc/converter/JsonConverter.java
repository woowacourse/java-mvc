package nextstep.mvc.converter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.mvc.exception.MvcException;

import java.util.Map;

public class JsonConverter {

    private static final JsonConverter instance = new JsonConverter();

    private final ObjectMapper objectMapper;

    private JsonConverter() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static JsonConverter getInstance() {
        return instance;
    }

    public String convert(Object object) {
        try {
            if (object instanceof Map) {
                Map map = objectMapper.convertValue(object, Map.class);
                return convertFromMap(map);
            }
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MvcException(String.format("Json 으로 변환에 실패했습니다. (%s)", object));
        }
    }

    public String convertFromMap(Map map) throws JsonProcessingException {
        if (map.size() == 1) {
            Object singleValue = map.values().iterator().next();
            return objectMapper.writeValueAsString(singleValue);
        }
        return objectMapper.writeValueAsString(map);
    }
}
