package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(
            Map<String, ?> model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        String jsonData = resolveJsonData(model);
        response.getWriter().write(jsonData);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private String resolveJsonData(Map<String, ?> model) {
        if (model.size() == 1) {
            Object singleData = model.values()
                    .iterator()
                    .next();
            return writeJsonData(singleData);
        }
        return writeJsonData(model);
    }

    private String writeJsonData(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json processing 과정에서 문제가 발생했습니다.", e);
        }
    }
}
