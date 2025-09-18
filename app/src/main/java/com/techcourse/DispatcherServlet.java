package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings = new ArrayList<>();
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);
        log.info("init dispatcherServlet!");
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();

        Object handler = null;

        for (HandlerMapping handlerMapping : handlerMappings) {
            handler = handlerMapping.getHandler(request);
            if (handler != null) {
                log.debug("Found handler using {}: {}",
                        handlerMapping.getClass().getSimpleName(), handler.getClass().getSimpleName());
                break;
            }
        }
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            if (handler instanceof Controller) {
                ModelAndView modelAndView = ((Controller) handler).execute(request, response);
                final View view = modelAndView.getView();
                view.render(modelAndView.getModel(), request, response);
            } else if (handler instanceof HandlerExecution) {
                ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
                final View view = modelAndView.getView();
                view.render(modelAndView.getModel(), request, response);
            } else {
                throw new ServletException("Unhandled handler type");
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
