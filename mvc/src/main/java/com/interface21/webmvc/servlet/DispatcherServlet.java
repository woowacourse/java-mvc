package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.handler.HandlerContainerFacade;
import com.interface21.webmvc.servlet.handler.HandlerProcessorFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerContainerFacade handlerContainerFacade;
    private final HandlerProcessorFacade handlerProcessorFacade;

    public DispatcherServlet(String basePackage) {
        this.handlerContainerFacade = new HandlerContainerFacade(basePackage);
        this.handlerProcessorFacade = new HandlerProcessorFacade();
    }

    @Override
    public void init() {
        log.info("DispatcherServlet initialized");
    }

    @Override
    protected void service(
            final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = handlerContainerFacade.getHandler(request);
            ModelAndView modelAndView = handlerProcessorFacade.process(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
