package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;


    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);

        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        handlerAdapters.add(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {

            HandlerMapping handler = handlerMappings.stream()
                    .filter(handlerMapping -> handlerMapping.getHandler(request) != null)
                    .findFirst()
                    .orElseThrow(() -> new ServletException("No HandlerMapping found for request URI : " + requestURI));

            HandlerAdapter adapter = handlerAdapters.stream()
                    .filter(handlerAdapter -> handlerAdapter.supports(handler.getHandler(request)))
                    .findFirst()
                    .orElse(null);

            if(adapter == null) {
                ModelAndView modelAndView = (ModelAndView)handler.getHandler(request);
                modelAndView.getView().render(modelAndView.getModel(), request, response);
                return;
            }

            ModelAndView modelAndView = adapter.handle(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}

