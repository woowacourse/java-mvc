package com.techcourse;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private HandlerManager handlerManager;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerManager = new HandlerManager(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(BASE_PACKAGE)
        );
        handlerManager.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        handlerManager.handle(request, response);
    }
}
