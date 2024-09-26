package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.getInstance();
    private final HandlerAdapterRegistry handlerAdapterRegistry = HandlerAdapterRegistry.getInstance();

    @Override
    public void init() {
        initHandlerMappingRegistry();
        initHandlerAdapterRegistry();
    }

    private void initHandlerMappingRegistry() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(DispatcherServlet.class);
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    private void initHandlerAdapterRegistry() {
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                log.info("No handler found for request");
                return;
            }

            HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            if (adapter == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.error("No adapter found for handler");
                return;
            }

            ModelAndView modelAndView = adapter.handle(request, response, handler);
            if (modelAndView == null || modelAndView.getView() == null) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.error("No modelAndView found for handler result");
                return;
            }

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);

        } catch (Exception e) {
            log.error("Servlet Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
