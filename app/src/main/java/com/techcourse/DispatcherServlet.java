package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.controller.ControllerAdaptor;
import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecutionAdaptor;
import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdaptor> handlerAdaptors = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);

        ControllerAdaptor controllerAdaptor = new ControllerAdaptor();
        handlerAdaptors.add(controllerAdaptor);

        HandlerExecutionAdaptor handlerExecutionAdaptor = new HandlerExecutionAdaptor();
        handlerAdaptors.add(handlerExecutionAdaptor);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .filter(Objects::nonNull)
                    .findAny()
                    .orElseThrow();

            HandlerAdaptor adaptor = handlerAdaptors.stream()
                    .filter(handlerAdaptor -> handlerAdaptor.canExecute(handler))
                    .findAny()
                    .orElseThrow();

            ModelAndView modelAndView = adaptor.execute(handler, request, response);

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
