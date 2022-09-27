package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String value = OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(getModelOrFirstValue(model));

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(value);
    }

    private Object getModelOrFirstValue(final Map<String, ?> model) {
        if (model.size() != 1) {
            return model;
        }
        return model.values()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("value 값이 존재하지 않습니다."));
    }
}
