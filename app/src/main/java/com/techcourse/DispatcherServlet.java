package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        final HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        this.handlerMappingRegistry.register(manualHandlerMapping);

        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        this.handlerMappingRegistry.register(annotationHandlerMapping);

        handlerAdapterRegistry.register(new ControllerHandlerAdapter());
        handlerAdapterRegistry.register(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
            final Object handler = handlerMapping.getHandler(request);

            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView mav = handlerAdapter.handle(handler, request, response);

            render(mav, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("핸들러를 처리하는데 실패했습니다.");
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) {
        try {
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new IllegalStateException("렌더링에 실패했습니다.");
        }
    }
}
