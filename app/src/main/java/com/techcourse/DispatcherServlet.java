package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
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

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        final HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        this.handlerMappings.add(manualHandlerMapping);

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                "com.techcourse.controller");
        annotationHandlerMapping.initialize();
        this.handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        // TODO : 어댑터 분리
        try {
            Object handler = getHandler(request);
            if (handler instanceof Controller) {
                final String viewName = ((Controller) handler).execute(request, response);
                final ModelAndView mav = new ModelAndView(new JspView(viewName));
                render(mav, request, response); // TODO : 모델 값 채우기
            } else if (handler instanceof HandlerExecution) {
                final ModelAndView mav = ((HandlerExecution) handler).handle(request, response);
                render(mav, request, response); // TODO : 모델 값 채우기
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("핸들러를 처리하는데 실패했습니다.");
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.getHandler(request) != null) {
                return handlerMapping.getHandler(request);
            }
        }
        throw new IllegalArgumentException("요청에 대한 핸들러를 찾을 수 없습니다.");
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) {
        try {
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("렌더링에 실패했습니다.");
        }
    }
}
