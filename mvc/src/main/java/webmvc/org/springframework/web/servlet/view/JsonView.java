package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        OBJECT_MAPPER.writeValue(response.getWriter(), model);
    }
}
