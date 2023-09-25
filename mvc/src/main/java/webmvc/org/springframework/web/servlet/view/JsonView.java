package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String jsonBody = convertModelToJson(model);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonBody);
        }
    }

    private String convertModelToJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return String.valueOf(model.values().iterator().next());
        } else {
            return objectMapper.writeValueAsString(model);
        }
    }

    @Override
    public String getViewName() {
        return null;
    }
}
