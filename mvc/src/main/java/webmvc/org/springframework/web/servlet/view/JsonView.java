package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String body = objectMapper.writeValueAsString(model);
        final PrintWriter writer = response.getWriter();
        writer.write(body);
    }

    @Override
    public boolean isRedirectView() {
        return false;
    }

    @Override
    public String getViewName() {
        return null;
    }

}
