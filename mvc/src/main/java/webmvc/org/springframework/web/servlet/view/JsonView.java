package webmvc.org.springframework.web.servlet.view;

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
        PrintWriter writer = response.getWriter();
        if (model.size() == 1) {
            response.setContentType(MediaType.PLAIN_TEXT_UTF8_VALUE);
            for (Map.Entry<String, ?> entry : model.entrySet()) {
                writer.write(entry.getValue().toString());
            }
        } else {
            String value = objectMapper.writeValueAsString(model);
            writer.write(value);
        }
    }

    @Override
    public String getName() {
        return "";
    }
}
