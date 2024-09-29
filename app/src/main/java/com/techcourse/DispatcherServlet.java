package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.exception.ArgumentHandlerExceptionResolver;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerExceptionResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final String BASE_PACKAGE = "com.techcourse.controller";
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public DispatcherServlet() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerMappingRegistry = new HandlerMappingRegistry(BASE_PACKAGE);
        handlerExceptionResolver = new ArgumentHandlerExceptionResolver();
    }

    @Override
    public void init() {
        handlerAdapterRegistry.initialize();
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        Object handler = null;
        try {
            handler = findHandler(request);
            HandlerAdapter handlerAdapter = findHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            renderViewWithModel(request, response, modelAndView);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, handler, e);
        }
    }

    private Object findHandler(HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No handler found for request URI: " + request.getRequestURI()));
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapterRegistry.getHandlerAdapter(handler).orElseThrow(
                () -> new IllegalArgumentException(
                        "No handler adapter found for handler: " + handler.getClass()));
    }

    private void renderViewWithModel(HttpServletRequest request, HttpServletResponse response,
                                     ModelAndView modelAndView)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }
}
