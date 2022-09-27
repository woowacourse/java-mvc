package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonData = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(model);

        response.getWriter().print(jsonData);
    }
}
