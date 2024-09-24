package com.techcourse;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterContainer;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingContainer;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingContainer handlerMappingContainer;
    private final HandlerAdapterContainer handlerAdapterContainer;

    public DispatcherServlet(
            HandlerMappingContainer handlerMappingContainer,
            HandlerAdapterContainer handlerAdapterContainer
    ) {
        this.handlerMappingContainer = handlerMappingContainer;
        this.handlerAdapterContainer = handlerAdapterContainer;
    }

    @Override
    public void init() {
        handlerMappingContainer.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingContainer.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterContainer.findHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response);
            modelAndView.getView().render(Map.of(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
