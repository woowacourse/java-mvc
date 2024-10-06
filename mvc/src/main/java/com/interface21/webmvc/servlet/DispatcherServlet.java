package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.handler.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.handler.HandlerExecutor;
import com.interface21.webmvc.servlet.handler.HandlerMapping;
import com.interface21.webmvc.servlet.handler.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final int NOT_FOUND_STATUS = 404;
    private static final List<HandlerAdapter> DEFAULT_HANDLER_ADAPTERS =
            List.of(new HandlerExecutionHandlerAdapter());

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerExecutor = new HandlerExecutor(DEFAULT_HANDLER_ADAPTERS);
    }

    @Override
    public void init() {
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                response.setStatus(NOT_FOUND_STATUS);
                return;
            }
            ModelAndView modelAndView = handlerExecutor.handle(request, response, handler.get());
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
