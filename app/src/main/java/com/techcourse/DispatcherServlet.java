package com.techcourse;

import com.interface21.core.Ordered;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.legacy.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.annotation.HandlerExecutionAdapter;

import java.util.List;
import java.util.Comparator;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this(createHandlerMappings(),
                List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter()));
    }

    DispatcherServlet(final List<HandlerMapping> handlerMappings, final List<HandlerAdapter> handlerAdapters) {
        // Stable sorting preserves registration order for equal priorities.
        this.handlerMappings = handlerMappings.stream()
                .sorted(Comparator.comparingInt(DispatcherServlet::getOrder))
                .toList();
        this.handlerAdapters = List.copyOf(handlerAdapters);
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private static List<HandlerMapping> createHandlerMappings() {
        final var annotation = new AnnotationHandlerMapping("com.techcourse.controller");
        annotation.setOrder(0);
        return List.of(annotation);
    }

    private static int getOrder(final HandlerMapping mapping) {
        return mapping instanceof Ordered ordered ? ordered.getOrder() : Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final var adapter = getHandlerAdapter(handler);
            final var result = adapter.handle(handler, request, response);
            result.getView().render(result.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping mapping : handlerMappings) {
            final var handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No adapter for handler: " + handler.getClass().getName()));
    }
}
