package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerAdapter> handlerAdapters;
    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        this.handlerMappings = new ArrayList<>();
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    private void initHandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ManualHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = findHandler(request);
            HandlerAdapter handlerAdapter = findHandlerAdapter(handler);

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView.getViewName(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.getHandler(request) != null)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("처리할 수 없는 요청입니다."))
                .getHandler(request);
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("요청을 처리할 수 있는 핸들러 어댑터가 없습니다."));
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
