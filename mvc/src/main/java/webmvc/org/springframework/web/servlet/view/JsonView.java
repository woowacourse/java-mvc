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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writeBody(model, response, writer);
        }

    }

    private void writeBody(Map<String, ?> model, HttpServletResponse response, PrintWriter writer) throws JsonProcessingException {
        if (model.size() == 1) {
            response.setContentType(MediaType.PLAIN_TEXT_UTF8_VALUE);
            writeBodyByPlainText(model, writer);
        } else {
            String value = objectMapper.writeValueAsString(model);
            writer.write(value);
        }
    }

    private void writeBodyByPlainText(Map<String, ?> model, PrintWriter writer) {
        for (Map.Entry<String, ?> entry : model.entrySet()) {
            writer.write(entry.getValue().toString());
        }
    }

    @Override
    public String getName() {
        return "";
    }
}
