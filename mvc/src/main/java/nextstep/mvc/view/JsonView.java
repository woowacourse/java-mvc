package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final int ONLYE_ONE_MODEL_LENGTH = 1;
    private static final int FIRST_INDEX = 0;

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("application/json;charset=UTF-8");

        final String modelData = generateModelToWrite(model);
        response.getWriter().write(modelData);
    }

    private String generateModelToWrite(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == ONLYE_ONE_MODEL_LENGTH) {
            return objectMapper.writeValueAsString(new ArrayList<>(model.values()).get(FIRST_INDEX));
        }
        return objectMapper.writeValueAsString(model);
    }
}
