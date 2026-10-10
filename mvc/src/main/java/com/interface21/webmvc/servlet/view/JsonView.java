package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1. 응답 헤더 및 인코딩 설정 (JSON 응답 명시)
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // 2. Model Map을 JSON 문자열로 변환
        String jsonResult = objectMapper.writeValueAsString(model);

        // 3. Response 출력 스트림을 얻어 클라이언트에 전송
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResult);
            writer.flush(); // 버퍼 비우기
        }
    }
}
