package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(String.valueOf(StandardCharsets.UTF_8));

        String json = generateJsonFromModel(model);
        log.debug("json : {}", json);

        PrintWriter writer = response.getWriter();
        writer.write(json);
    }

    private String generateJsonFromModel(Map<String, ?> model) throws JsonProcessingException {
        if (model.keySet().size() == 1) {
            String key = model.keySet().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Model has any values"));
            log.debug("key : {}", key);
            return model.get(key).toString();
        }
        return objectMapper.writeValueAsString(model);
    }
}
