package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private final static Logger log = LoggerFactory.getLogger(JsonView.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Object dataToWrite;

        if (model != null && model.size() == 1) {
            Object singleValue = model.values().iterator().next();
            dataToWrite = validateNull(singleValue);
        } else {
            dataToWrite = model;
        }
        objectMapper.writeValue(response.getWriter(), dataToWrite);
    }

    private static Object validateNull(Object singleValue) {
        Object dataToWrite;
        if (singleValue == null) {
            dataToWrite = Collections.emptyMap();
        } else {
            dataToWrite = singleValue;
        }
        return dataToWrite;
    }
}
