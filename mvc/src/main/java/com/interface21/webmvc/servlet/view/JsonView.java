package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");

        Object body;
        if (model.size() == 1) {
            // 값이 1개뿐이면, 그 value만 꺼내서 JSON 직렬화
            body = model.values().iterator().next();
        } else {
            // 값이 2개 이상이면 Map 전체를 JSON 직렬화
            body = model;
        }

        String json = objectMapper.writeValueAsString(body);

        try(PrintWriter printWriter = response.getWriter()){
            printWriter.write(json);
            printWriter.flush();
        }
    }
}
