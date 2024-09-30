package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final int SINGLE_MODEL_SIZE = 1;
    private static final String ENCODING_TYPE = "UTF-8";
    private static final String CONTENT_TYPE = "application/json";

    private final JsonMapper jsonMapper;

    public JsonView() {
        jsonMapper = new JsonMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object writeValue = extractFromModel(model, jsonMapper);

        write(response, writeValue);
    }

    private static Object extractFromModel(Map<String, ?> model, JsonMapper jsonMapper) {
        try {
            if (model.size() == SINGLE_MODEL_SIZE) {
                return model.values()
                        .iterator()
                        .next();
            }
            return jsonMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Json으로 파싱하기에 실패했습니다.");
        }
    }

    private void write(HttpServletResponse response, Object writeValue) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

            response.getWriter().write(writeValue.toString());
        } catch (IOException e) {
            System.out.println(e);
            throw new IllegalArgumentException("response를 작성하는 것에 실패했습니다.");
        }
    }
}
