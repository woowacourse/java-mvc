package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.initializer.HandlerInitializer;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.DefaultRootPathStrategy;
import com.interface21.webmvc.servlet.mvc.tobe.registry.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.registry.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        HandlerInitializer handlerInitializer = new HandlerInitializer(new DefaultRootPathStrategy());
        initHandlerMappings(handlerInitializer);
        initHandlerAdapters(handlerInitializer);
    }

    private static void initHandlerAdapters(HandlerInitializer handlerInitializer) {
        Set<HandlerAdapter> initializedHandlerAdapters = handlerInitializer.getInitAdapters();
        HandlerAdapterRegistry.addHandlerAdapters(initializedHandlerAdapters);
    }

    private void initHandlerMappings(HandlerInitializer handlerInitializer) {
        Set<HandlerMapping> initializedHandlerMappings = handlerInitializer.getInitMappings();
        HandlerMappingRegistry.addHandlerMappings(initializedHandlerMappings);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}", request.getMethod());
        try {
            Object handler = HandlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalStateException("해당하는 Controller를 찾을 수 없습니다."));
            HandlerAdapter handlerAdapter = HandlerAdapterRegistry.getHandlerAdapter(handler)
                    .orElseThrow(() -> new IllegalStateException("해당하는 Adapter를 찾을 수 없습니다."));

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JspView jspView = (JspView) modelAndView.getView();
        jspView.render(modelAndView.getModel(), request, response);
    }
}
