package nextstep.mvc.view;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
        final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
        });

        String body = generateBody(model);
        response.getWriter().write(body);
    }

    private String generateBody(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object object = model.values().toArray()[0];
            return mapper.writeValueAsString(object);
        }

        return mapper.writeValueAsString(model);
    }
}
