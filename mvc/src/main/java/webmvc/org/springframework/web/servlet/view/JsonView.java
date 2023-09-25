package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String jsonValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);

        response.getWriter().write(jsonValue);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
