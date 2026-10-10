package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMethodNotSupportedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet(
            final HandlerMappingRegistry handlerMappingRegistry,
            final HandlerExecutor handlerExecutor
    ) {
        this.handlerMappingRegistry = Objects.requireNonNull(handlerMappingRegistry);
        this.handlerExecutor = Objects.requireNonNull(handlerExecutor);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalStateException("No handler found for " + requestURI));
            final var modelAndView = handlerExecutor.execute(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (RequestMethodNotSupportedException e) {
            handleMethodNotSupported(e, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private void handleMethodNotSupported(final RequestMethodNotSupportedException exception,
                                         final HttpServletResponse response) throws IOException {
        final String allowedMethods = exception.getSupportedMethods().stream()
                .map(Enum::name)
                .sorted()
                .collect(Collectors.joining(", "));
        response.setHeader("Allow", allowedMethods);
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

}
