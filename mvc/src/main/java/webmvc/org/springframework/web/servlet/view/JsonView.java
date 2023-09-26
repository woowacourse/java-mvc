package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final Writer writer = response.getWriter();

        final List<?> values = new ArrayList<>(model.values());
        if (values.size() == 1) {
            objectMapper.writeValue(writer, values.get(0));
            return;
        }

        objectMapper.writeValue(writer, model);
    }
}
