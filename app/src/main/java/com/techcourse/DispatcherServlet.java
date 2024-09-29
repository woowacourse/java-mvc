package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.CompositeHandlerMapping;
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

    public static final String DEFAULT_BASE_PACKAGE = "com.techcourse.controller";

    private CompositeHandlerMapping compositeHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(DEFAULT_BASE_PACKAGE);
        annotationHandlerMapping.initialize();

        this.compositeHandlerMapping = new CompositeHandlerMapping(
                List.of(manualHandlerMapping, annotationHandlerMapping)
        );
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = compositeHandlerMapping.getHandler(request);
            ModelAndView modelAndView = executeHandler(handler, request, response);
            resolveView(modelAndView, request, response);
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            throw new ServletException(exception);
        }
    }

    private ModelAndView executeHandler(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } else if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        throw new IllegalArgumentException("실행할 핸들러가 존재하지 않습니다. : " + handler.getClass().getName());
    }

    private void resolveView(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
