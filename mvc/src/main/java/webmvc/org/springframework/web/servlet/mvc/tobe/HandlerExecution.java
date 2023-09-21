package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

  private final Method method;
  private final Object target;

  public HandlerExecution(final Method method, final Object target) {
    this.method = method;
    this.target = target;
  }

  public ModelAndView handle(
      final HttpServletRequest request,
      final HttpServletResponse response
  ) {
    try {
      return (ModelAndView) method.invoke(target, request, response);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalArgumentException("Reflection Exception");
    }
  }
}
