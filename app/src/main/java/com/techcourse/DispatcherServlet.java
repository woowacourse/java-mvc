package com.techcourse;

import com.interface21.webmvc.HandlerMappings;
import com.interface21.webmvc.RequestMappingHandlerAdapter;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.SimpleControllerHandlerAdapter;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String BASE_PACKAGE = DispatcherServlet.class.getPackageName();
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE));
        handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new SimpleControllerHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new RequestMappingHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerMapping handlerMapping = handlerMappings.getHandlerMapping(request);
            Object handler = handlerMapping.getHandler(request);

            HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(handler, request, response);

            View view = new JspView(modelAndView.getViewName());
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
