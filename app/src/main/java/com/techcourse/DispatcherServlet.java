package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        // 기존 Legacy MVC의 수동 매핑을 초기화한다.
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        // @Controller 기반의 새로운 MVC 매핑을 초기화한다.
        final AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        // 서로 다른 매핑 방식을 같은 HandlerMapping 타입으로 관리한다.
        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            // 여러 HandlerMapping 중 현재 요청을 처리할 Handler를 찾는다.
            final Object handler = getHandler(request);

            // Handler 종류에 따라 실행하고 ModelAndView로 통일한다.
            final ModelAndView modelAndView = handle(handler, request, response);

            // Controller 실행 이후 View에게 렌더링을 위임한다.
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        throw new IllegalArgumentException("요청을 처리할 Handler가 없습니다: " + request.getRequestURI());
    }

    private ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // Legacy MVC Controller는 String viewName을 반환한다.
        if (handler instanceof Controller controller) {
            final String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        }

        // 새로운 @MVC는 HandlerExecution이 ModelAndView를 반환한다.
        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.handle(request, response);
        }

        throw new IllegalArgumentException("지원하지 않는 Handler입니다: " + handler.getClass().getName());
    }
}
