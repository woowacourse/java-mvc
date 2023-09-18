package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

  boolean isSupport(final Object handler);

  Object handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler
  );
}
