package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void render(
      final Map<String, ?> model,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {
    final Object responseBody;
    if (model.size() == 1) {
      responseBody = model.values().iterator().next();
    } else {
      responseBody = model;
    }

    final String json = objectMapper.writeValueAsString(responseBody);
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter().write(json);
  }
}
