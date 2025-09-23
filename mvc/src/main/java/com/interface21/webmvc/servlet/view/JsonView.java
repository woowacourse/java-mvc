package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class JsonView implements View {

    /**
     * Jackson 라이브러리의 ObjectMapper: 객체를 JSON 문자열로 변환하는 핵심 도구
     * https://www.baeldung.com/jackson-object-mapper-tutorial
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        try {
            // HTTP 응답에 쓰기 위한 PrintWriter 가져오기
            PrintWriter writer = response.getWriter();
            // JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            // 모델 Map을 JSON 문자열로 변환
            String responseJson = convertToJson(model);
            // 변환한 JSON을 응답으로 씀
            writer.write(responseJson);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 모델 데이터를 JSON 문자열로 변환 (직렬화)
     * @param model
     * @return String
     * @throws IOException
     */
    private String convertToJson(Map<String, ?> model) throws IOException {
        // model에 데이터가 1개라면 값만 추출해 JSON 직렬화
        if (model.size() == 1) {
            Object value = model.values().iterator().next();
            return objectMapper.writeValueAsString(value);
        }
        // 여러 개면 Map 전체를 직렬화
        return objectMapper.writeValueAsString(model);
    }

}


