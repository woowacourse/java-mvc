package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.HandlerAdapter;
import webmvc.org.springframework.web.servlet.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping());

        handlerAdapters.add(new ManulHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());

        for (final var handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var handler = getHandler(request);
            final var adapter = getAdapter(handler);

            final var modelAndView = adapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

}
