package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            Map<String, ?> model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String responseBody = objectMapper.writeValueAsString(model);
        response.getWriter().write(responseBody);
    }

}
