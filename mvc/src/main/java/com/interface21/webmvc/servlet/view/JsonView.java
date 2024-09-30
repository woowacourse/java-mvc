package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            HttpServletResponse response
    ) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        handleResponse(response, model);
    }

    private void handleResponse(final HttpServletResponse response, final Map<String, ?> model) {
        if (model.size() == 1) {
            model.values().forEach(data -> sendResponse(response, data));
            return;
        }
        sendResponse(response, model);
    }

    private void sendResponse(final HttpServletResponse response, final Object data) {
        try {
            response.getWriter().write(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new IllegalStateException("Http 응답을 보내는 중 문제가 발생하였습니다.");
        }
    }
}
