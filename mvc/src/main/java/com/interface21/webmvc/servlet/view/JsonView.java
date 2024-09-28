package com.interface21.webmvc.servlet.view;

import static com.interface21.web.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    public static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(model);

        log.debug("json data: {}", result);

        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(result);
    }
}
