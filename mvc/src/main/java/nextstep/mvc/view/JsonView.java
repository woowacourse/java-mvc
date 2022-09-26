package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            String serialized = mapper.writeValueAsString(model);
            response.getWriter().write(serialized);
        } catch (IOException e) {
            throw new RuntimeException("Fails on write model as json", e);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
