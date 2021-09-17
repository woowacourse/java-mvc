package nextstep.mvc.view;

import static nextstep.web.support.MediaType.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import nextstep.web.support.ContentType;
import nextstep.web.support.MediaType;
import nextstep.web.support.StatusCode;

public class JsonView implements View {

    private final Map<String, Object> newModel;
    private final StatusCode statusCode;
    private final ObjectMapper objectMapper;

    public JsonView() {
        this(new HashMap<>(), StatusCode.OK);
    }

    public JsonView(Map<String, Object> newModel, StatusCode statusCode) {
        this.newModel = newModel;
        this.statusCode = statusCode;
        this.objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(statusCode.statusNumber());
        response.setHeader(ContentType.headerKey(), APPLICATION_JSON_UTF8_VALUE);
        String jsonBody = resolveJson(model);
        response.getWriter().append(jsonBody);
        response.flushBuffer();
    }

    private String resolveJson(Map<String, Object> model) {
        try {
            model.putAll(newModel);
            if (model.size() == 1) {
                return objectMapper.writeValueAsString(
                    model.values().iterator().next()
                );
            }

            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("can not mapping object to json");
        }
    }
}
