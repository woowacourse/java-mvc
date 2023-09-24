package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_SIZE = 1;
    private final ObjectMapper mapper;

    public JsonView() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();

        try {
            if (model.size() == SINGLE_SIZE) {
                final Object onlyValue = model.values().toArray()[0];
                mapper.writeValue(writer, onlyValue);
            }
            mapper.writeValue(writer, model);
        } catch (IOException e) {
            throw new RuntimeJsonMappingException("json을 값을 write할 수 없습니다.");
        }
    }
}
