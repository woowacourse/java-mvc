package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        String responseBody = parse(model);
        PrintWriter writer = response.getWriter();
        writer.write(responseBody);

        log.info("렌더링 완료");
    }

    private String parse(Map<String, ?> model) throws JsonProcessingException {
        Map<String, Object> parseResult = new HashMap<>();

        for (String key : model.keySet()) {
            parseResult.put(key, model.get(key));
        }

        return parseBody(parseResult);
    }

    private static String parseBody(Map<String, Object> parseResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (parseResult.size() == 1) {
            log.info("전달된 값이 하나입니다.");
            Object value = parseResult.values()
                    .stream()
                    .findFirst()
                    .orElse(null);
            return objectMapper.writeValueAsString(value);
        }
        return objectMapper.writeValueAsString(parseResult);
    }
}
