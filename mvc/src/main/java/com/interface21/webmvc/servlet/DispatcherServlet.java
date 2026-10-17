package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final String basePackage;
    private final HandlerExceptionResolver exceptionResolver;

    public DispatcherServlet(final String basePackage, final HandlerExceptionResolver exceptionResolver) {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.basePackage = basePackage;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    public void init() {
        initHandlerMappingRegistry();
        initHandlerAdapterRegistry();
    }

    private void initHandlerMappingRegistry() {
        // 실제 controller가 있는 "com.techcourse" 전달
        final var annotationHandlerMapping = new AnnotationHandlerMapping(this.basePackage);
        annotationHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    private void initHandlerAdapterRegistry() {
        final var handlerExecutionAdapter = new HandlerExecutionAdapter();
        handlerAdapterRegistry.addHandlerAdapter(handlerExecutionAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalStateException("등록된 핸들러가 없습니다: " + request.getRequestURI()));

            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView mav = handlerAdapter.handle(handler, request, response);
            mav.getView().render(mav.getModel(), request, response);

        } catch (Throwable e) {
            final Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            if (exceptionResolver.resolve(cause, response)) {
                return;
            }
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
