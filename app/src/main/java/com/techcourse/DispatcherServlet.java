package com.techcourse;

import com.techcourse.handleradapter.SimpleHandlerAdapter;
import com.techcourse.handlermapping.CombinedHandlerMapping;
import com.techcourse.viewresolver.SimpleViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String CONTROLLER_BASE_PACKAGE = "com.techcourse.controller";

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;
    private ViewResolver viewResolver;

    @Override
    public void init() {
        handlerMapping = new CombinedHandlerMapping(CONTROLLER_BASE_PACKAGE);
        handlerMapping.initialize();
        handlerAdapter = new SimpleHandlerAdapter();
        viewResolver = new SimpleViewResolver();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        Object handler = handlerMapping.getHandler(request);
        if (handler == null || !handlerAdapter.supports(handler)) {
            throw new ServletException("No handler for " + request.getRequestURI());
        }

        try {
            handle(request, response, handler);
        } catch (Exception e) {
            log.error("Error while handling request: {}", e.getMessage(), e);
            throw new ServletException("Error while processing request", e);
        }
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object mv = handlerAdapter.handle(request, response, handler);
        viewResolver.resolveView(mv, request, response);
    }
}
