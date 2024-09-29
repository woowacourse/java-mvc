package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(
            Map<String, ?> model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        handleResponse(response, model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private void handleResponse(HttpServletResponse response, Map<String, ?> model) {
        if (model.values().size() == 1) {
            model.values()
                    .forEach(data -> writeJsonData(response, data));
            return;
        }
        writeJsonData(response, model);
    }

    private void writeJsonData(HttpServletResponse response, Object data) {
        try {
            String jasonData = mapper.writeValueAsString(data);
            response.getWriter().write(jasonData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json processing 과정에서 문제가 발생했습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException("json write 과정에서 문제가 발생했습니다.", e);
        }
    }
}
