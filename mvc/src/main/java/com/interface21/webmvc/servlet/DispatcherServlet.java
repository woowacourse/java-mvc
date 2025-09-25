package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.view.ViewRenderer;
import com.interface21.webmvc.servlet.mvc.view.ViewResolverRegistry;
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

    private final HandlerExecutor handlerExecutor = new HandlerExecutor();
    private final ViewResolverRegistry viewResolverRegistry = new ViewResolverRegistry();
    private final ViewRenderer viewRenderer = new ViewRenderer();
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
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
                if (handler instanceof HandlerExecution handlerExecution) {
                    final var result = handlerExecutor.execute(handlerExecution, request, response);
                    final var modelAndView = viewResolverRegistry.resolve(result);
                    viewRenderer.render(modelAndView, request, response);
                    return;
                }
            }
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
