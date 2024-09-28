package com.techcourse;

import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterContainer;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingContainer;
import com.interface21.webmvc.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final View PAGE_404_VIEW = new JspView("redirect:/404.jsp");
    private static final long serialVersionUID = 1L;

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
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final String requestURI = request.getRequestURI();
            log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

            Object handler = handlerMappingContainer.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterContainer.findHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            handleException(e, request, response);
        }
    }

    private void handleException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Exception: {}", e.getMessage(), e);
        try {
            PAGE_404_VIEW.render(Map.of(), request, response);
        } catch (Exception ex) {
            log.error("Exception during rendering 404 page: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
}
