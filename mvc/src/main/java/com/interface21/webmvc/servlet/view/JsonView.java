package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Set<String> keySet = model.keySet();

        String body = parseBody(model, keySet);

        PrintWriter writer = response.getWriter();
        writer.write(body);
    }

    private String parseBody(Map<String, ?> model, Set<String> keySet) throws JsonProcessingException {
        Map<String, Object> results = new HashMap<>();
        for (String key : keySet) {
            results.put(key, model.get(key));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        if (results.size() == 1) {
            return objectMapper.writeValueAsString(results.values().iterator().next());
        }

        return objectMapper.writeValueAsString(results);
    }
}
