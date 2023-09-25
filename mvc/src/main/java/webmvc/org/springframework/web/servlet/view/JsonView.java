package webmvc.org.springframework.web.servlet.view;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int MINIMUM_SIZE = 1;
    private static final int FIRST_INDEX = 0;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        try (PrintWriter writer = response.getWriter()) {
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);
            writer.write(toJson(model));
        } catch (IOException e) {
            throw new IllegalArgumentException("Json 렌더링 중 예외가 발생했습니다.", e);
        }
    }

    private String toJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == MINIMUM_SIZE) {
            Object[] values = model.values().toArray();
            return OBJECT_MAPPER.writeValueAsString(values[FIRST_INDEX]);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
