package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;

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
            new AnnotationHandlerMapping("com.techcourse")
        )
    );

    handlerAdapterComposite = new HandlerAdapters(
        List.of(
            new AnnotationHandlerAdapter()
        )
    );
  }

  @Override
  protected void service(
      final HttpServletRequest request,
      final HttpServletResponse response
  ) throws ServletException {
    final String requestURI = request.getRequestURI();
    log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

    try {
      final Object handler = handlerMappingComposite.getHandler(request);
      final Object handleValue = handlerAdapterComposite.handle(request, response, handler);

      final ModelAndView modelAndView = (ModelAndView) handleValue;

      final View view = modelAndView.getView();
      view.render(modelAndView.getModel(), request, response);

    } catch (Throwable e) {
      log.error("Exception : {}", e.getMessage(), e);
      throw new ServletException(e.getMessage());
    }
  }
}
