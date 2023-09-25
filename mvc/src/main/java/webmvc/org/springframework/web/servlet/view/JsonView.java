package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String jsonValue = objectMapper.writeValueAsString(model);
        if (model.size() == 1) {
            response.getWriter().write(
                    jsonValue.substring(jsonValue.indexOf("{", 1), jsonValue.lastIndexOf("}")));
            return;
        }
        response.getWriter().write(jsonValue);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
