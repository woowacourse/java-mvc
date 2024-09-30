package com.interface21.webmvc.servlet.view;

import static com.interface21.web.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    public static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String result = serializeToJson(model);
        log.debug("data: {}", result);

        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(result);
    }

    private String serializeToJson(Map<String, ?> model) throws JsonProcessingException {
        Object target = model;
        if (model.size() == 1) {
            target = model.values().stream()
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
        return objectMapper.writeValueAsString(target);
    }
}
