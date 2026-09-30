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
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(Object... basePackages) {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        init(basePackages);
    }

    public void init(Object... basePackage) {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        addHandlerMapping(annotationHandlerMapping);

        HandlerExecutionAdapter handlerExecutionAdapter = new HandlerExecutionAdapter();
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        addHandlerAdapter(handlerExecutionAdapter);
        addHandlerAdapter(controllerHandlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        this.handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if(handler.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
