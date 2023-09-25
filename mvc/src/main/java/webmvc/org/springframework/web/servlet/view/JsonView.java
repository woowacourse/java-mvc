package webmvc.org.springframework.web.servlet.view;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (model == null || model.isEmpty()) {
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String body = new ObjectMapper().writeValueAsString(model);
        final PrintWriter writer = response.getWriter();
        writer.write(body);
    }
}
