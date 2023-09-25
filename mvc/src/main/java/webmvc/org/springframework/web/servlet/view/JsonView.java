package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String jsonBody = convertModelToJson(model);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentLength(jsonBody.length());

        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertModelToJson(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return String.valueOf(model.values().iterator().next());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(model);
    }
}
