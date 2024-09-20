package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final String BASE_PACKAGE = "com.techcourse.controller";
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new HandlerMappings(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(BASE_PACKAGE)
        );
        handlerAdapters = new HandlerAdapters(
                new ManualHandlerAdapter(),
                new AnnotationHandlerAdapter()
        );
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = createModelAndView(request, response);
            modelAndView.renderView(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView createModelAndView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = handlerMappings.getHandler(request);
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        return handlerAdapter.handle(request, response, handler);
    }
}
