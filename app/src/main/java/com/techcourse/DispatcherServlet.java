package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.Execution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final String CONTROLLER_PACKAGE = "com.techcourse.controller";
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMappings(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(CONTROLLER_PACKAGE)
        );

        handlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Execution handler = handlerMappings.getHandler(request);
            final ModelAndView modelAndView = handler.handle(request, response);
            modelAndView.renderView(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
