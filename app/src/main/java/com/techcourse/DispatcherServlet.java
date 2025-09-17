package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        final HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        this.handlerMappingRegistry.register(manualHandlerMapping);

        final HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        this.handlerMappingRegistry.register(annotationHandlerMapping);

        handlerAdapters.addAll(List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            final ModelAndView mav = handle(handler, request, response);
            render(mav, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("핸들러를 처리하는데 실패했습니다.");
        }
    }

    private ModelAndView handle(final Object handler, final HttpServletRequest request,
                                final HttpServletResponse response) {
        try {
            // TODO : 스트림으로 바꾸기
            for (HandlerAdapter handlerAdapter : handlerAdapters) {
                if (handlerAdapter.isAdaptable(handler)) {
                    return handlerAdapter.handle(handler, request, response);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("핸들러를 실행하는데 실패했습니다.");
        }
        throw new IllegalStateException("핸들러를 실행하는데 실패했습니다.");
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
