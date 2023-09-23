package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        final String body = JsonWriterObjectMapper.writeJson(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    @Override
    public String getName() {
        return null;
    }
}
