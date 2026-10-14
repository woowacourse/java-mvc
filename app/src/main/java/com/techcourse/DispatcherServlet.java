package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.SimpleControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    DispatcherServlet(final List<HandlerMapping> handlerMappings, final List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = List.copyOf(handlerMappings);
        this.handlerAdapters = List.copyOf(handlerAdapters);
    }

    @Override
    public void init() throws ServletException {
        try {
            final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
            annotationHandlerMapping.initialize();
            final var manualHandlerMapping = new ManualHandlerMapping();
            manualHandlerMapping.initialize();

            handlerMappings = List.of(annotationHandlerMapping, manualHandlerMapping);
            handlerAdapters = List.of(new HandlerExecutionHandlerAdapter(), new SimpleControllerHandlerAdapter());
        } catch (Exception e) {
            throw new ServletException("Failed to initialize handler mappings", e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var handler = getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final var handlerAdapter = getHandlerAdapter(handler);
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final var handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException("No adapter supports handler: " + handler.getClass().getName());
    }
}
