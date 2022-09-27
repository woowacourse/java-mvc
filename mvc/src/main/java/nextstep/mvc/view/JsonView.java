package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);

        if (model.size() == 1) {
            final Object key = model.keySet().toArray()[0];
            final Object value = model.get(key);
            jsonValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        }

        response.getWriter().write(jsonValue);
    }
}
