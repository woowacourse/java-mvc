package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

  private static final Logger log = LoggerFactory.getLogger(JsonView.class);
  private static final int SINGLE_VALUE_INDEX = 0;

  @Override
  public void render(
      final Map<String, ?> model,
      final HttpServletRequest request,
      final HttpServletResponse response
  ) throws Exception {
    final ObjectMapper objectMapper = new ObjectMapper();
    final String body = objectMapper.writeValueAsString(model);

    try {
      if (hasSingle(model)) {
        response.setContentType("text/plain");

        response.getWriter().write(String.valueOf(model.values().toArray()[SINGLE_VALUE_INDEX]));
        return;
      }

      response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
      response.getWriter().write(body);
    } catch (IOException e) {
      log.error("JSON IOException Error", e);
    }

  }

  private boolean hasSingle(final Map<String, ?> model) {
    return model.size() == 1;
  }
}
