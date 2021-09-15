package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import nextstep.web.support.MediaType;

public class JsonView implements View {

    public static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if(model.size() == 1) {
            model.values().forEach(v -> writeToResponse(response, toJson(v)));
            return;
        }

        writeToResponse(response, objectMapper.writeValueAsString(model));
    }

    private String toJson(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void writeToResponse(HttpServletResponse response, String value) {
        try {
            response.getWriter().write(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
