package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptorRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdaptor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdaptorRegistry handlerAdaptorRegistry = new HandlerAdaptorRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        try {
            AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
            ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

            handlerMappingRegistry.addHandler(annotationHandlerMapping);
            handlerMappingRegistry.addHandler(manualHandlerMapping);

            handlerMappingRegistry.initialize();

            AnnotationHandlerAdaptor annotationHandlerAdaptor = new AnnotationHandlerAdaptor();
            ManualHandlerAdaptor manualHandlerAdaptor = new ManualHandlerAdaptor();

            handlerAdaptorRegistry.addHandlerAdaptor(annotationHandlerAdaptor);
            handlerAdaptorRegistry.addHandlerAdaptor(manualHandlerAdaptor);

        } catch (InvocationTargetException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler);

        ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
