package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.exception.ViewException;

import java.util.Map;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String json = getResponseBody(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
    }

    private String getResponseBody(final Map<String, ?> model) {
        if (model.size() == 1) {
            return String.valueOf(model.values().toArray()[0]);
        }
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            log.warn("Model\\[ {} \\] 을 Json으로 파싱하던 도중에 오류가 발생하였습니다.", model, e);
            throw new ViewException("[ERROR] Model을 Json으로 파싱하던 도중에 오류가 발생하였습니다.");
        }
    }
}
