package com.interface21.web;

import com.interface21.webmvc.servlet.ViewConverter;
import com.interface21.webmvc.servlet.mvc.HandlerKeys;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerContainer;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final HandlerKeys handlerKeys;
    private final String[] basePackages;

    public DispatcherServlet(final Package... packages) {
        this(Arrays.stream(packages)
                .map(Package::getName)
                .toArray(String[]::new));
    }

    public DispatcherServlet(final String... basePackages) {
        this.handlerKeys = new HandlerKeys();
        this.handlerMappings = new ArrayList<>();
        this.basePackages = basePackages;
    }

    @Override
    public void init() {
        initialize();
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void initialize() {
        final ControllerContainer container = new ControllerContainer(basePackages);
        container.initialize();
        handlerMappings.add(new AnnotationHandlerMapping(handlerKeys, container));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            final var controller = handlerKeys.get(new HandlerKey(request));
            final var result = controller.handle(request, response);
            final var view = ViewConverter.convert(result);

            view.render(result.getModel(), request, response);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
