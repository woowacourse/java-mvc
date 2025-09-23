package com.techcourse;

import com.interface21.webmvc.servlet.mvc.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterImpl;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
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
    HandlerAdapterImpl handlerAdapter = new HandlerAdapterImpl();
    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            for (HandlerMapping handlerMapping : handlerMappings) {
                final var handler = handlerMapping.getHandler(request);
                handlerAdapter.handle(request, response, handler);
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
