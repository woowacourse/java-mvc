package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse";

    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() {
        try {
            annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            HandlerExecution handler = annotationHandlerMapping.getHandler(request);
            ModelAndView handle = handler.handle(request, response);
            View view = handle.getView();
            view.render(handle.getModel(), request, response);
        } catch (Throwable e) {
            throw new ServletException(e.getMessage());
        }
    }
}
