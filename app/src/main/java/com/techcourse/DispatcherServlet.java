package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerAdapters;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.HandlerMappings;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMappings(
                new AnnotationHandlerMapping(basePackage),
                new ControllerHandlerMapping(basePackage)
        );
        handlerAdapters = new HandlerAdapters();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView mv = handlerAdapter.invoke(handler, request, response);
            View view = mv.getView();
            view.render(mv.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    public void appendHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.appendHandlerMapping(handlerMapping);
    }
}
