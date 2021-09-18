package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (model.size() == 1) {
            final Object responseBodyObject = model.entrySet()
                    .iterator()
                    .next()
                    .getValue();
            responseJson(response, responseBodyObject);
            return;
        }
        responseJson(response, model);
    }

    private void responseJson(HttpServletResponse response, Object responseBodyObject) throws IOException {
        final String responseBody = OBJECT_MAPPER.writeValueAsString(responseBodyObject);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(responseBody);
    }
}
