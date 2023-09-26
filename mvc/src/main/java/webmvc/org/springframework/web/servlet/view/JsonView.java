package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final int FIRST_VALUE_INDEX = 0;
    private static final int SINGLE_RESPONSE_VALUE = 1;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEXT_HTML_TYPE = "text/html";

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = getResponseBody(model, response);
        response.getWriter().write(body);
    }

    private String getResponseBody(final Map<String, ?> model, final HttpServletResponse response) throws JsonProcessingException {
        if (model.size() == SINGLE_RESPONSE_VALUE) {
            Object data = model.values().toArray()[FIRST_VALUE_INDEX];
            response.setContentType(TEXT_HTML_TYPE);
            return String.valueOf(data);
        }

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return objectMapper.writeValueAsString(model);
    }
}
