package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappers = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappers.add(new ManualHandlerMapping());
        handlerMappers.add(new AnnotationHandlerMapping("com.techcourse.controller"));

        for (final HandlerMapping handlerMapping : handlerMappers) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = findController(request);
            final var controllerExecution = findControllerExecution(handler.getClass());
            final var viewName = controllerExecution.invoke(handler, request, response);
            move(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findController(final HttpServletRequest request) {
        return handlerMappers.stream()
                      .map(handlerMapping -> handlerMapping.getHandler(request))
                      .filter(Objects::nonNull)
                      .findFirst()
                      .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 handler입니다."));
    }

    private Method findControllerExecution(final Class<?> controllerType) {
        return Arrays.stream(controllerType.getDeclaredMethods())
                     .filter(DispatcherServlet::isExecuteMethod)
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("컨트롤러 실행 메서드가 존재하지 않습니다."));
    }

    private static boolean isExecuteMethod(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 2
                && parameterTypes[0].equals(HttpServletRequest.class)
                && parameterTypes[1].equals(HttpServletResponse.class);
    }

    private void move(final Object view, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // TODO : 3단계 미션에서 리팩토링 필요
        if (view.getClass().equals(String.class)) {
            moveString((String) view, request, response);
        }
        if (view.getClass().equals(HandlerExecution.class)) {
            moveHandlerExecution((ModelAndView) view, request, response);
        }
    }

    private void moveString(final String view, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (view.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(view.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }

    private void moveHandlerExecution(final ModelAndView view, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String name = view.getView().getClass().getName();
        moveString(name, request, response);
    }
}
