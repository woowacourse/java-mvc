package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
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

    private final String basePackage;
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(final String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        final var annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        annotationHandlerMapping.initialize();
        handlerMappings = List.of(annotationHandlerMapping);
        handlerAdapters = List.of(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                throw new ServletException("No handler for " + request.getMethod() + " " + requestURI);
            }
            final HandlerAdapter adapter = getHandlerAdapter(handler);
            if (adapter == null) {
                throw new ServletException("No handler adapter for " + handler.getClass().getName());
            }
            final ModelAndView modelAndView = adapter.handle(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        return null;
    }
}
