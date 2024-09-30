package com.interface21.webmvc.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    public final AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet(String basePackage) {
        annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
    }

    @Override
    public void init() {
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final HandlerExecution handler = (HandlerExecution) annotationHandlerMapping.getHandler(request);
            final ModelAndView modelAndView = handler.handle(request, response);
            modelAndView.render(request, response);
        } catch (final Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
