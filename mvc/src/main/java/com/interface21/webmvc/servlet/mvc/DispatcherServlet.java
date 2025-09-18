package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.mapping.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.mapping.annotation.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.handler.resolver.HandlerResolver;
import com.interface21.webmvc.servlet.mvc.view.resolver.ViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private HandlerResolver handlerResolver;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final String basePackage = getServletConfig().getInitParameter("basePackage");

        this.handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.interface21.webmvc.servlet.mvc.controller", basePackage)
        );
        this.handlerMappings.forEach(HandlerMapping::initialize);
        this.handlerResolver = new HandlerResolver(new ViewResolver());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "not supported uri: %s %s".formatted(request.getMethod(), request.getRequestURI())));
            final ModelAndView modelAndView = handlerResolver.resolve(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
