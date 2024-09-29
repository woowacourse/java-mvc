package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    List<HandlerMapping> handlerMappings;
    List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
        handlerAdapters = List.of(new ManualHandlerAdapter(), new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        Object handler = getHandler(request);
        HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        try {
            modelAndView.renderView(request, response);
        } catch (Exception e) {
            throw new RuntimeException("view render과정에서 예외가 발생했습니다: " + e);
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Object foundHandler = handlerMapping.getHandler(request);
            if (foundHandler != null) {
                return foundHandler;
            }
        }
        throw new IllegalStateException("request에 적합한 Handler를 찾지 못했습니다");
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.isSupported(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException("request에 적합한 HandlerAdapter를 찾지 못했습니다.");
    }
}
