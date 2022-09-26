package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import javassist.NotFoundException;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int SINGLE_ATTRIBUTE = 1;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Object attributes = getAttributes(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(attributes));
    }

    private Object getAttributes(final Map<String, ?> model) {
        if (model.size() == SINGLE_ATTRIBUTE) {
            return model.values()
                    .stream()
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("model을 찾을 수 없습니다."));
        }

        return model;
    }
}
