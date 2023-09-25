package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {

        String body = objectMapper.writeValueAsString(model);
        log.info("JSON : {}", body);
        response.getWriter().write(body);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
