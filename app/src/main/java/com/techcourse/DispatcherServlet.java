package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
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
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping(Application.class));
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request,
                           final HttpServletResponse response) throws ServletException {
        logRequest(request);
        try {
            final Object handler = getHandler(request);
            final ModelAndView modelAndView = executeHandler(handler, request, response);
            renderView(modelAndView, request, response);
        } catch (Throwable e) {
            handleException(e);
        }
    }

    private void logRequest(final HttpServletRequest request) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.findHandler(request)
                .orElseThrow(() -> new IllegalArgumentException("제공하지 않는 요청입니다: " + request));
    }

    private ModelAndView executeHandler(final Object handler,
                                        final HttpServletRequest request,
                                        final HttpServletResponse response) throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        return handlerAdapter.handle(request, response, handler);
    }

    private void renderView(final ModelAndView modelAndView,
                            final HttpServletRequest request,
                            final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private void handleException(final Throwable e) throws ServletException {
        log.error("Exception : {}", e.getMessage(), e);
        throw new ServletException(e.getMessage(), e);
    }
}
