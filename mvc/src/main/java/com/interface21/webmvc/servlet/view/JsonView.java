package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper;
    private final JsonEncoding encoding = JsonEncoding.UTF8;
    private boolean disableCaching = true;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        prepareResponse(response);

        OutputStream stream = response.getOutputStream();

        writeContent(stream, model);
    }

    private void prepareResponse(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(this.encoding.getJavaName());
        if (this.disableCaching) {
            response.addHeader("Cache-Control", "no-store");
        }
    }

    private void writeContent(OutputStream stream, Map<String, ?> model) throws IOException {
        try (JsonGenerator generator = this.objectMapper.getFactory().createGenerator(stream, this.encoding)) {
            ObjectWriter objectWriter = this.objectMapper.writer();

            Object value = model;
            if (model.size() == 1) {
                value = model.values().iterator().next();
            }

            objectWriter.writeValue(generator, value);

            generator.flush();
        }
    }

    public void setDisableCaching(boolean disableCaching) {
        this.disableCaching = disableCaching;
    }
}
