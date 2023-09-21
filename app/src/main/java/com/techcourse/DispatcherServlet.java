package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;
import webmvc.org.springframework.web.servlet.mvc.tobe.ManualHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

  private HandlerMapping handlerMappingComposite;
  private HandlerAdapter handlerAdapterComposite;

  public DispatcherServlet() {
  }

  @Override
  public void init() {
    handlerMappingComposite = new HandlerMappings(
        List.of(
            new ManualHandlerMapping(),
            new AnnotationHandlerMapping("com.techcourse")
        )
    );

    handlerAdapterComposite = new HandlerAdapters(
        List.of(
            new ManualHandlerAdapter(),
            new AnnotationHandlerAdapter()
        )
    );
  }

  @Override
  protected void service(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException {
    final String requestURI = request.getRequestURI();
    log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

    try {
      final Object handler = handlerMappingComposite.getHandler(request);
      final Object handleValue = handlerAdapterComposite.handle(request, response, handler);

      // TODO : 3단계 리팩터링
      renderByHandlerValueType(request, response, handleValue);

    } catch (Throwable e) {
      log.error("Exception : {}", e.getMessage(), e);
      throw new ServletException(e.getMessage());
    }
  }

  private void renderByHandlerValueType(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handleValue
  ) throws Exception {
    if (handleValue instanceof ModelAndView) {
      final ModelAndView modelAndView = (ModelAndView) handleValue;
      move(modelAndView.getView().getName(), request, response);
    }

    if (handleValue instanceof String) {
      final String viewName = (String) handleValue;
      move(viewName, request, response);
    }
  }

  private void move(final String viewName, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {
    if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
      response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
      return;
    }

    final var requestDispatcher = request.getRequestDispatcher(viewName);
    requestDispatcher.forward(request, response);
  }
}
