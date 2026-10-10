package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry(new ArrayList<>());
        this.handlerAdapterRegistry = new HandlerAdapterRegistry(new ArrayList<>());
    }

    @Override
    public void init() {
        initHandlerMappingRegistry();
        initHandlerAdapterRegistry();
    }

    private void initHandlerMappingRegistry() {
        // 실제 controller가 있는 "com.techcourse" 전달
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();

        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);


    }

    private void initHandlerAdapterRegistry() {
        final var controllerHandlerAdapter = new ControllerHandlerAdapter();
        final var handlerExecutionAdapter = new HandlerExecutionAdapter();

        handlerAdapterRegistry.addHandlerAdapter(controllerHandlerAdapter);
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
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
