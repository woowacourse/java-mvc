package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new ManualHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = mapHandler(request);

            ModelAndView modelAndView = handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object mapHandler(HttpServletRequest request) throws ServletException {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.findHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new ServletException("No handler found for requestURI: " + request.getRequestURI());
    }

    private ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.isSupport(handler)) {
                return handlerAdapter.handle(request, response, handler);
            }
        }
        throw new ServletException("No handler found for requestURI: " + request.getRequestURI());
    }
}
