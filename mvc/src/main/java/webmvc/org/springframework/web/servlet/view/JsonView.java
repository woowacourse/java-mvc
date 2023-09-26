package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = extractJsonString(model, objectMapper);
        PrintWriter printWriter = response.getWriter();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        printWriter.write(jsonString);
    }

    private String extractJsonString(Map<String, ?> model, ObjectMapper objectMapper) throws JsonProcessingException {
        if (model.size() == 1) {
            Object value = model.values().toArray()[0];
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(model);
    }
}
