package com.interface21.webmvc.servlet.mvc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws
        ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);
            final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final var modelAndView = handlerAdapter.handle(handler, request, response);

            final Map<String, Object> model = modelAndView.getModel();
            final View view = modelAndView.getView();
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
            .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 핸들러입니다."));
    }
}
