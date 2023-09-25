package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = convertModelToString(model);
        response.setCharacterEncoding(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private String convertModelToString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object obj = model.values().toArray()[0];
            return String.valueOf(obj);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(model);
    }
}
