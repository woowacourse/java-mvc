package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.adapter.AnnotationHandlerAdapter;
import com.techcourse.adapter.HandlerAdapter;
import com.techcourse.adapter.HandlerAdapterRegistry;
import com.techcourse.adapter.ManualHandlerAdapter;
import com.techcourse.mapping.HandlerMappingRegistry;
import com.techcourse.mapping.ManualHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(getClass().getPackageName());
        HandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerAdapterRegistry.addHandlerAdapter(manualHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Exception ex) {
            throw new ServletException("예상치 못한 예외가 발생했습니다.");
        }
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
