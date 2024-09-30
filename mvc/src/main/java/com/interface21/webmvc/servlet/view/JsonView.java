package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String body = writeJson(model);
        log.info("body: {}", body);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(body);
        response.getWriter().flush();
    }

    private String writeJson(final Map<String, ?> model) throws JsonProcessingException {
        if (model.isEmpty()) {
            return objectMapper.writeValueAsString(Collections.emptyMap());
        }
        if (model.size() == 1) {
            return objectMapper.writeValueAsString(getFirstData(model));
        }
        return objectMapper.writeValueAsString(model);
    }

    private Object getFirstData(final Map<String, ?> model) {
        return model.values()
                .stream()
                .findFirst()
                .orElseThrow();
    }
}
