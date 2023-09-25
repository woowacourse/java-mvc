package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

  private static final int SINGLE_VALUE_INDEX = 0;

  @Override
  public void render(
      final Map<String, ?> model,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) throws Exception {
    final ObjectMapper objectMapper = new ObjectMapper();
    final String body = objectMapper.writeValueAsString(model);

    if (hasSingle(model)) {
      response.setContentType("text/plain");

      response.getWriter().write(String.valueOf(model.values().toArray()[SINGLE_VALUE_INDEX]));
      return;
    }

    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    response.getWriter().write(body);
  }

  private boolean hasSingle(final Map<String, ?> model) {
    return model.size() == 1;
  }
}
