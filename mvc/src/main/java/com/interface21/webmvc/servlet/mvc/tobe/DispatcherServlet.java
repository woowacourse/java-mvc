package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        Object handler = getHandler(request);
        ModelAndView modelAndView = executeHandler(request, response, handler);
        render(modelAndView, request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        for (var handlerMapping : handlerMappingRegistry.getHandlerMappings()) {
            Object handler;
            if ((handler = handlerMapping.getHandler(request)) != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("handler does not exists");
    }

    private ModelAndView executeHandler(HttpServletRequest request, HttpServletResponse response,
                                        Object handler) {
        ModelAndView modelAndView;
        try {
            for (var handlerAdapter : handlerAdapterRegistry.getHandlerAdapters()) {
                if (handlerAdapter.supports(handler)) {
                    modelAndView = handlerAdapter.handle(request, response, handler);
                    return modelAndView;
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("exception occured" + e.getMessage());
        }

        throw new IllegalArgumentException("No HandlerAdapter found for handler: " + handler);
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();

        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to render view" + e.getMessage());
        }
    }
}
