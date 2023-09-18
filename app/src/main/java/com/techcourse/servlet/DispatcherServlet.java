package com.techcourse.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.handler.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse"));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = mapHandler(request);
            final HandlerAdapter handlerAdapter = mapHandlerAdapter(handler);
            final Object view = handlerAdapter.execute(request, response);

            move((String) view, request, response);
            
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object mapHandler(final HttpServletRequest request) {
        return handlerMappings.stream().filter(handlerMapping -> handlerMapping.getHandler(request) != null)
                .findFirst()
                .orElse(handlerMappings.get(0))
                .getHandler(request);
    }

    private HandlerAdapter mapHandlerAdapter(final Object handler) {
        if (handler instanceof Controller) {
            log.info("this is controller");
            return new ControllerHandlerAdapter(handler);
        }
        if (handler instanceof HandlerExecution) {
            log.info("this is handlerExecution");
            return new HandlerExecutionHandlerAdapter(handler);
        }
        throw new IllegalArgumentException();
    }


    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
