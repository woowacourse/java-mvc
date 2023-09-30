package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        model.keySet().forEach(key -> request.setAttribute(key, model.get(key)));
        final PrintWriter writer = response.getWriter();
        final String jsonResult = toResultBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        writer.write(jsonResult);
    }

    private String toResultBody(final Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            final Object value = model.values().toArray()[0];
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public String getViewName() {
        return null;
    }
}
