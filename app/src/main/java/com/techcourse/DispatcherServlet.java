package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerManager;
import com.interface21.webmvc.servlet.mvc.tobe.ServletRequestHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerManager handlerManager;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ServletRequestHandler annotationServletRequestHandlerMapping = new AnnotationHandlerMapping(
                Application.class);
        ServletRequestHandler manualServletRequestHandlerMapping = new ManualHandlerMapping();
        handlerManager = new HandlerManager(annotationServletRequestHandlerMapping, manualServletRequestHandlerMapping);
        handlerManager.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            handlerManager.handle(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
