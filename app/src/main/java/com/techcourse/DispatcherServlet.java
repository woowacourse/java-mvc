package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.ManualHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = List.of(new AnnotationHandlerMapping("com.techcourse"), new ManualHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);
        handlerAdapters = List.of(new AnnotationHandlerAdapter(), new ManualHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final Object result = handlerAdapter.handle(request, response, handler);
            processResult(result, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new ServletException("No handler matches your request."));
    }
    private HandlerAdapter getHandlerAdapter(final Object handler) throws ServletException {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("No handler Adapter matches your request."));
    }

    private void processResult(final Object result, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (result instanceof ModelAndView) {
            final View view = ((ModelAndView) result).getView();
            if (view instanceof JspView) {
                move(((JspView) view).getViewName(), request, response);
            }
            // TODO: jsonView
        }
        if (result instanceof String) {
            move((String) result, request, response);
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
