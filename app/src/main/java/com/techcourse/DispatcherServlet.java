package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.annotation.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    public static final String RESOURCES_BASE_PACKAGE = "com.techcourse";

    private final HandlerMappingRegistry handlerMappings;
    private final HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappingRegistry();
        this.handlerAdapters = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(RESOURCES_BASE_PACKAGE);
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        annotationHandlerMapping.initialize();
        manualHandlerMapping.initialize();

        HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        handlerMappings.addMapping(annotationHandlerMapping);
        handlerMappings.addMapping(manualHandlerMapping);
        handlerAdapters.addAdapter(annotationHandlerAdapter);
        handlerAdapters.addAdapter(manualHandlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = executeController(request, response);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage());
        }
    }

    private ModelAndView executeController(HttpServletRequest request, HttpServletResponse response) {
        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler, request, response);
            return handlerAdapter.handle(handler, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        String viewName = view.getViewName();

        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
