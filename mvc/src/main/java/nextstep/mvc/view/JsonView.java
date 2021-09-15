package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() <= 1) {
            Optional<?> firstValue = model.values().stream()
                .findFirst();
            String jsonValue = objectMapper.writeValueAsString(firstValue.get());
            response.getWriter().write(jsonValue);
            return;
        }
        String value = objectMapper.writeValueAsString(model);
        response.getWriter().write(value);
    }
}
