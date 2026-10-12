package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.ViewResolver;
import com.interface21.webmvc.servlet.view.ViewResolverRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private final ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
    private HandlerExecutor handlerExecutor;

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addViewResolver(final ViewResolver viewResolver) {
        viewResolverRegistry.addViewResolver(viewResolver);
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final ModelAndView modelAndView = handlerExecutor.execute(handler.get(), request, response);
            render(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        final View view = resolveView(modelAndView.getViewName());
        view.render(modelAndView.getModel(), request, response);
    }

    private View resolveView(final String viewName) {
        return viewResolverRegistry.resolveViewName(viewName)
                .orElseThrow(() -> new IllegalStateException("No ViewResolver for view name: " + viewName));
    }
}
