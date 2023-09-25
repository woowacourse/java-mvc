package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.*;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerExecution noHandlerFoundHandler;
    private final Set<HandlerMapping> handlerMappings;

    public DispatcherServlet(Collection<HandlerMapping> handlerMappings, HandlerExecution noHandlerFoundHandler) {
        this.handlerMappings = new HashSet<>(handlerMappings);
        this.noHandlerFoundHandler = noHandlerFoundHandler;
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            HandlerExecution handlerExecution = findHandlerMappingFor(request)
                    .map(it -> it.getHandler(request))
                    .map(it -> (HandlerExecution) it)
                    .orElse(noHandlerFoundHandler);

            ModelAndView modelAndView = handlerExecution.handle(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Optional<HandlerMapping> findHandlerMappingFor(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(it -> it.supports(request))
                .findFirst();
    }
}
