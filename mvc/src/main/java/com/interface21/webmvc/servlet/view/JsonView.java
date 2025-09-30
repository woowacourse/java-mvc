package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    /***
     *
     * JSON을 자바 객체로 변환할 때 Jackson 라이브러리를 사용한다.
     * Jackson 라이브러리 공식 문서를 읽어보고 사용법을 익힌다.
     * JSON으로 응답할 때 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE으로 반환해야 한다.
     * model에 데이터가 1개면 값을 그대로 반환하고 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.
     */

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        try (PrintWriter writer = response.getWriter()) {
            if (model.size() == 1) {
                Object next = model.values().iterator().next();
                writer.print(next);
                return;
            }

            String json = mapper.writeValueAsString(model);
            writer.print(json);
        }
    }
}
