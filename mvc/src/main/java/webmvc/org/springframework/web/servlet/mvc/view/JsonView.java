package webmvc.org.springframework.web.servlet.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        final String result = objectMapper.writeValueAsString(model.get("model"));

        final PrintWriter writer = response.getWriter();
        writer.write(result);
    }
}
