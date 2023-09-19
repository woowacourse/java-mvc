package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.AnnotationControllerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.asis.InterfaceControllerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private transient HandlerMappingRegistry handlerMappingRegistry;
    private transient HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        this.handlerMappingRegistry = getHandlerMappingRegistry();
        this.handlerAdapterRegistry = getHandlerAdapterRegistry();
    }

    private HandlerMappingRegistry getHandlerMappingRegistry() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.addHandlerMapping(
            getInitializedHandlerMapping(new ManualHandlerMapping()));
        registry.addHandlerMapping(
            getInitializedHandlerMapping(new AnnotationHandlerMapping("com")));
        return registry;
    }

    private HandlerAdapterRegistry getHandlerAdapterRegistry() {
        HandlerAdapterRegistry registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(new AnnotationControllerAdapter());
        registry.addHandlerAdapter(new InterfaceControllerAdapter());
        return registry;
    }

    private HandlerMapping getInitializedHandlerMapping(HandlerMapping handlerMapping) {
        handlerMapping.initialize();
        return handlerMapping;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Optional<Object> registryHandler = handlerMappingRegistry.getHandler(request);
            if (registryHandler.isEmpty()) {
                handlerNotFound(request, response);
                return;
            }
            Object handler = registryHandler.get();
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler)
                .orElseThrow(() -> new IllegalStateException("핸드러를 처리할 어댑터가 없습니다."));
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handlerNotFound(HttpServletRequest request, HttpServletResponse response) {
        try {
            JspView jspView = new JspView("/404.jsp");
            jspView.render(Collections.emptyMap(), request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("404 페이지 출력 실패", e);
        }
    }
}
