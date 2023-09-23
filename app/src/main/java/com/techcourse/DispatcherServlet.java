package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappingRegistry handlerMappingRegistry;
    private final transient HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(HandlerMappingRegistry handlerMappingRegistry, HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
        if (handlerAdapterRegistry == null) {
            throw new IllegalStateException("어뎁터 레지스트리는 필수로 등록되어야 합니다.");
        }
        if (handlerMappingRegistry == null) {
            throw new IllegalStateException("핸들러 매핑 레지스트리는 필수로 등록되어야 합니다.");
        }
    }

    @Override
    public void init() {
        logger.info("initialized DispatcherServlet");
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler)
                .orElseThrow(() -> new IllegalStateException("핸들러를 처리할 어댑터가 없습니다."));
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
