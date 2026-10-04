package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.handler.adapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.handler.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private HandlerMappingRegistry handlerMappingRegistry;

    private ControllerHandlerAdapter  controllerHandlerAdapter;
    private HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter;
    private HandlerAdapterRegistry  handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappingRegistry = HandlerMappingRegistry.empty();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        controllerHandlerAdapter = new ControllerHandlerAdapter();
        handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();

        handlerAdapterRegistry = HandlerAdapterRegistry.empty();
        handlerAdapterRegistry.addHandlerAdapter(controllerHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(handlerExecutionHandlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            final var controller = manualHandlerMapping.getHandler(request);
            final var viewName = ((Controller) controller).execute(request, response);
            JspView jspView = new JspView(viewName);
            jspView.render(Map.of(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    protected void serviceV2(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        Object handler = getHandler(request);
        ModelAndView modelAndView;

        try{
            if (handler instanceof Controller) {
                String viewName = ((Controller) handler).execute(request, response);
                modelAndView = new ModelAndView(new JspView(viewName));
            } else if (handler instanceof HandlerExecution) {
                modelAndView = ((HandlerExecution) handler).handle(request, response);
            } else {
                throw new RuntimeException("처리할 수 없는 핸들러입니다. : " + handler.getClass());
            }

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    protected void serviceV3(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try{
            Object handler = getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new RuntimeException(request.getRequestURL().toString() + "에 해당하는 핸들러를 찾을 수 없습니다"));
    }

}
