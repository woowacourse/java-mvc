package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class ManualHandlerAdapter implements HandlerAdapter {

  @Override
  public boolean isSupport(final Object handler) {
    return handler instanceof Controller;
  }

  @Override
  public Object handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler
  ) {
    try {
      return ((Controller) handler).execute(request, response);
    } catch (Exception e) {
      throw new IllegalArgumentException("ManualHandlerAdapter execute Exception");
    }
  }
}
