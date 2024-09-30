package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handlerAdapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handlerAdapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.handlerAdapter.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handlerMapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handlerMapping.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        initHandlerMappingRegistry();
        initHandlerAdapterRegistry();
    }

    private void initHandlerMappingRegistry() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(Application.class);
        annotationHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    private void initHandlerAdapterRegistry() {
        HandlerExecutionHandlerAdapter executionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(executionHandlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        ModelAndView modelAndView = adapter.handle(handler, request, response);
        try {
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new IllegalStateException("요청을 처리하던 중 예외가 발생했습니다: " + e.getMessage());
        }
    }
}
