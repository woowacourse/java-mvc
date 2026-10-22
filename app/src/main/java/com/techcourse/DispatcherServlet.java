package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initializeHandlerMappings();
        initializeHandlerAdapters();
    }

    private void initializeHandlerMappings() {
        handlerMappingRegistry = new HandlerMappingRegistry();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

        annotationHandlerMapping.initialize();
        manualHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
    }

    private void initializeHandlerAdapters() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();

        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Object actualHandler = handler.get();
            HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(actualHandler);
            ModelAndView modelAndView = adapter.handle(request, response, actualHandler);

            modelAndView.getView().render(modelAndView.getModel(), request, response);

        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
