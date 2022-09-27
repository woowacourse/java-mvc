package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request, HttpServletResponse response) throws Exception {
        logModelEntry(model);
        String body = convertModelToJsonString(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
    }

    private String convertModelToJsonString(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return firstModelValue(model);
        }

        return objectMapper.writeValueAsString(model);
    }

    private String firstModelValue(final Map<String, ?> model) {
        return model.values().toArray()[0].toString();
    }

    private void logModelEntry(final Map<String, ?> model) {
        model.keySet()
                .forEach(key -> log.debug("attribute name : {}, value : {}", key, model.get(key)));
    }
}
