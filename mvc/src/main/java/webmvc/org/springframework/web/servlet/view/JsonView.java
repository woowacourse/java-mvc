package webmvc.org.springframework.web.servlet.view;

import static web.org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int MINIMUM_SIZE = 1;
    private static final String JSON_FORMAT = "{\"%s\":\"%s\"}";

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            response.setContentType(APPLICATION_JSON_UTF8_VALUE);
            outputStream.write(toJson(model).getBytes());
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalArgumentException("Json 렌더링 중 예외가 발생했습니다.", e);
        }
    }

    private String toJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == MINIMUM_SIZE) {
            return model.keySet().stream()
                    .map(key -> String.format(JSON_FORMAT, key, model.get(key)))
                    .collect(Collectors.joining());
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }

}
