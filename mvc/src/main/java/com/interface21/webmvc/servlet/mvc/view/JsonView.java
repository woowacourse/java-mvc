package com.interface21.webmvc.servlet.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        final String jsonOutput = objectMapper.writeValueAsString(model);
        log.debug("json body: {}", jsonOutput);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(jsonOutput.getBytes(StandardCharsets.UTF_8));
    }
}
