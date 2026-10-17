package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

  private final String basePackage;
  private List<HandlerMapping> handlerMappings;
  private List<HandlerAdapter> handlerAdapters;

  public DispatcherServlet(final String basePackage) {
    this.basePackage = basePackage;
  }

  @Override
  public void init() {
    final AnnotationHandlerMapping annotationHandlerMapping =
        new AnnotationHandlerMapping(basePackage);
    annotationHandlerMapping.initialize();

    handlerMappings = List.of(annotationHandlerMapping);
    handlerAdapters = List.of(new HandlerExecutionAdapter());
  }

  @Override
  protected void service(
      final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
    log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

    try {
      final Object handler = getHandler(request);
      if (handler == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }

      final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
      final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
      modelAndView.getView().render(modelAndView.getModel(), request, response);
    } catch (Exception exception) {
      log.error("Exception : {}", exception.getMessage(), exception);
      throw new ServletException(exception);
    }
  }

  private Object getHandler(final HttpServletRequest request) {
    for (HandlerMapping handlerMapping : handlerMappings) {
      final Object handler = handlerMapping.getHandler(request);
      if (handler != null) {
        return handler;
      }
    }
    return null;
  }

  private HandlerAdapter getHandlerAdapter(final Object handler) {
    for (HandlerAdapter handlerAdapter : handlerAdapters) {
      if (handlerAdapter.supports(handler)) {
        return handlerAdapter;
      }
    }
    throw new IllegalStateException(
        "No HandlerAdapter supports handler: " + handler.getClass().getName());
  }
}
