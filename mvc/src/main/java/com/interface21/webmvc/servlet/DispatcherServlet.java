package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.View;
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
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();

        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
        log.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());

        try {
            final Object handler = getHandler(req);
            if (handler == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final HandlerAdapter adapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(req, res, handler);
            render(modelAndView, req, res);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest req) {
        return handlerMappingRegistry.getHandler(req);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapterRegistry.getHandlerAdapter(handler);
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, res);
    }
}
