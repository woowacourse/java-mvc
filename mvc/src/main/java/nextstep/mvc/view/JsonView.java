package nextstep.mvc.view;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(parseToJson(model));
    }

    private String parseToJson(final Map<String, ?> model) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(extract(model));
    }

    private Object extract(final Map<String, ?> model) {
        if(model.size() > 1) {
            return model;
        }

        return model.values().stream()
            .findFirst()
            .orElseThrow();
    }
}
