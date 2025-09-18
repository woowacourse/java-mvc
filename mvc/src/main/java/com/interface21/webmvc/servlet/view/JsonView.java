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
        // model에 데이터가 1개면 값을 그대로 반환
        if (model.size() == 1) {
            // 값들을 순회하기 위해 Iterator 생성
            Iterator<?> modelIterator = model.values().iterator();
            StringBuilder responseJson = new StringBuilder();

            // 모델의 값(객체)을 StringBuilder에 붙임
            while(modelIterator.hasNext()) {
                responseJson.append(modelIterator.next());
            }

            // 값 안에서 JSON 문자열 시작 위치('{')를 찾아 substring 함
            int jsonStartIndex = responseJson.indexOf("{");

            // substring 한 JSON 문자열을 다시 ObjectMapper를 통해 직렬화
            return objectMapper.writeValueAsString(responseJson.substring(jsonStartIndex));
        }

        // 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환
        return objectMapper.writeValueAsString(model);
    }
}


