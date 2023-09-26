package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MappingJackson2HttpMessageConverter;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final var contents = MappingJackson2HttpMessageConverter.convertFromModel(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentLength(contents.length());
        print(response.getWriter(), contents);
    }

    private void print(final PrintWriter writer, final String contents) {
        writer.print(contents);
        writer.flush();
    }

}
