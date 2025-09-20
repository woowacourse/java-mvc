package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
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
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                "com.techcourse.controller");
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        final AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(manualHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRegistry.getHandler(request).orElseThrow(ServletException::new);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
