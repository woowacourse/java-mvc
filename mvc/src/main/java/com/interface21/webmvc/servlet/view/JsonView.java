package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonView implements View {

    @Override
    public void render(final HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter printWriter = new PrintWriter(response.getOutputStream())) {

            ArrayList<String> attributeNames = Collections.list(request.getAttributeNames());
            if (attributeNames.size() == 1) {
                String name = attributeNames.getFirst();
                Object attribute = request.getAttribute(name);

                objectMapper.writeValue(printWriter, attribute);
                return;
            }

            Map<String, Object> attributes = attributeNames.stream()
                .collect(Collectors.toMap(
                    name -> name,
                    request::getAttribute
                ));
            objectMapper.writeValue(printWriter, attributes);
        } catch (Exception e) {
            throw new RuntimeException("응답 처리 중 문제가 발생했습니다.");
        }
    }

    @Override
    public String getViewName() {
        return "";
    }
}
