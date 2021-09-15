package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final int status;

    public JsonView(final int status) {
        this.status = status;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter().write(bodyByModelSize(model));
    }

    private String bodyByModelSize(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return OBJECT_MAPPER.writeValueAsString(model.values().toArray()[0]);
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
