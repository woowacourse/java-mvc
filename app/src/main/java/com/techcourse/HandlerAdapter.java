package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapter.class);

    private List<HandlerMapping> handlerMappings;

    public void init() {
        handlerMappings = new ArrayList<>();
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
    }

    public void service(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException {
        Object handler = null;
        String requestUri = request.getRequestURI();

        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(request);
            if (handler != null) {
                log.debug("Found handler using {}: {}",
                        handlerMapping.getClass().getSimpleName(), handler.getClass().getSimpleName());
                break;
            }
        }
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        try {
            ModelAndView modelAndView;
            if (handler instanceof Controller) {
                modelAndView = ((Controller) handler).execute(request, response);
            } else if (handler instanceof HandlerExecution) {
                modelAndView = ((HandlerExecution) handler).handle(request, response);
            } else {
                throw new ServletException("Unhandled handler type");
            }
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
