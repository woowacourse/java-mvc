package com.interface21.webmvc.servlet.view;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Object> data;

    public JsonView(final Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public void render(
            final Map<String, ?> model,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        handleResponse(response);
    }

    private void handleResponse(final HttpServletResponse response) {
        if (data.size() == 1) {
            data.values().forEach(data -> sendResponse(response, data));
            return;
        }
        sendResponse(response, data);
    }

    private void sendResponse(final HttpServletResponse response, final Object data) {
        try {
            response.getWriter().write(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new IllegalStateException("Http 응답을 보내는 중 문제가 발생하였습니다.");
        }
    }
}
