package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonView() {
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        log.info("json view render");
        model.forEach((k, v) -> log.info("attribute name : {}, value : {}", k, v));

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter writer = response.getWriter();
        if (model.size() <= 1) {
            model.forEach((k, v) -> writer.print(v));
            writer.close();
            return;
        }
        String json = objectMapper.writeValueAsString(model);
        writer.print(json);
        writer.close();
    }
}
