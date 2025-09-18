package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
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

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        addHandlerAdapter();
        addHandlerMapping();
    }

    private void addHandlerAdapter() {
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionAdapter());
    }

    private void addHandlerMapping() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        this.handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new ServletException("No handler found for request"));
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();

        view.render(model, request, response);
    }
}
