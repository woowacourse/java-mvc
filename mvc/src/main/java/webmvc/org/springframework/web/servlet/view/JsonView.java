package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String requestBody = getResponseBody(model);
        log.debug("requestBody json value : {}", requestBody);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(requestBody);
    }

    private String getResponseBody(final Map<String, ?> model) {
        final ObjectMapper objectMapper = new ObjectMapper();
        if (model.size() == 1) {
            return String.valueOf(model.values().toArray()[0]);
        }
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("json 타입으로 파싱할 수 없습니다.");
        }
    }
}
