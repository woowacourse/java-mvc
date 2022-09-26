package nextstep.mvc.view;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String jsonData = objectMapper.writeValueAsString(model);

        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter()
                .write(jsonData);
    }
}
