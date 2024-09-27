package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.AbstractView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView extends AbstractView {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static JsonView getInstance() {
        return new JsonView("jsonView");
    }

    public JsonView(String viewName) {
        super(viewName);
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String json = modelToJson(model);
        response.getWriter().write(json);
    }

    private String modelToJson(Map<String, ?> model) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
