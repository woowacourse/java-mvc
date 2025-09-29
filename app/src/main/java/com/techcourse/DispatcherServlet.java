package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerSelector;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.processor.HandlerProcessorFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerSelector> handlerSelectors = new ArrayList<>();
    private final HandlerProcessorFacade handlerProcessorFacade = new HandlerProcessorFacade();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerSelectors.add(manualHandlerMapping);
        AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping(this.getClass().getPackageName());
        annotationHandlerMapping.initialize();
        handlerSelectors.add(annotationHandlerMapping);
    }

    @Override
    protected void service(
            final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = handlerSelectors.stream()
                    .map(mapping -> mapping.getHandler(request))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new ServletException("Handler Not Found!"));

            ModelAndView modelAndView = handlerProcessorFacade.process(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
