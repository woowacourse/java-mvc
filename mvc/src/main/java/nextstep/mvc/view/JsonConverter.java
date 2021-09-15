package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.mvc.exception.view.JsonConvertingException;

public class JsonConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new JsonConvertingException();
        }
    }
}
