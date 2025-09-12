package com.techcourse;

import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
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

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = List.of(
                new AnnotationHandlerMapping("com.techcourse.controller"),
                new ManualHandlerMapping()
        );
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);
            final var modelAndView = executeHandler(handler, request, response);
            View view = modelAndView.getView();

            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청을 처리할 수 없습니다."));
    }

    private ModelAndView executeHandler(final Object handler, final HttpServletRequest request,
                                        final HttpServletResponse response)
            throws Exception {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }

        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(request, response);
            JspView jspView = new JspView(viewName);

            return new ModelAndView(jspView);
        }

        throw new IllegalStateException("handler does not support");
    }
}
