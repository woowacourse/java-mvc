package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

  @Override
  public boolean isSupport(final Object handler) {
    return handler instanceof HandlerExecution;
  }

  @Override
  public Object handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler
  ) {
    return ((HandlerExecution) handler).handle(request, response);
  }
}
