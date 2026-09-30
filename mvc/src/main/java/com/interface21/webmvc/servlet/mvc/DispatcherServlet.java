package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackages;
    private final List<HandlerMapping> additionalMappings;
    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(Object... basePackages) {
        this.basePackages = basePackages;
        this.additionalMappings = new ArrayList<>();
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackages);
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        for (HandlerMapping mapping : additionalMappings) {
            mapping.initialize();
            handlerMappingRegistry.addHandlerMapping(mapping);
        }

        addHandlerAdapter(new HandlerExecutionAdapter());
        addHandlerAdapter(new ControllerHandlerAdapter());
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.additionalMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if(handler.isEmpty()) {
                log.warn("No handler found for request URL {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler.get());
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler.get());
            render(modelAndView, request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
