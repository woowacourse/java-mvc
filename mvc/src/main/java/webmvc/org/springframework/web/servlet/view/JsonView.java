package webmvc.org.springframework.web.servlet.view;

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
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String value;
        if (model.size() == 1) {
            value = String.valueOf(model.values().toArray()[0]);
        } else {
            value = objectMapper.writeValueAsString(model);
        }
        PrintWriter writer = response.getWriter();
        writer.write(value);
    }

    @Override
    public String viewName() {
        return null;
    }
}
