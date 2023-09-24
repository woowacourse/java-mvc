package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        if (model.isEmpty()) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }

        final String responseBody = objectMapper.writeValueAsString(model);
        response.getWriter().write(responseBody);
    }
}
