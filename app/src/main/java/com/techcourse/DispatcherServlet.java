package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serial;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
        handlerAdapters = List.of(new ControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);
            final var handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);

            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No handler found for " + request.getMethod() + " " + request.getRequestURI()
                ));
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No HandlerAdapter found for " + handler.getClass().getName()
                ));
    }
}
