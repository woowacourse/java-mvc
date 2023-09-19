package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappers;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final Class<HandlerMapping> clazz = HandlerMapping.class;
        final Class<?>[] handlerMappingInterfaces = clazz.getInterfaces();

        handlerMappers = Arrays.stream(handlerMappingInterfaces)
                               .map(handler -> {
                                   try {
                                       final Object instance = handler.getConstructor().newInstance();
                                       instance.getClass()
                                               .getMethod("initialize")
                                               .invoke(instance.getClass());
                                       return (HandlerMapping) instance;
                                   } catch (Exception exception) {
                                       throw new RuntimeException("생성자에 접근할 수 없습니다.");
                                   }
                               })
                               .collect(Collectors.toList());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Class<?> controllerType = findController(request);
            final var controllerExecution = findControllerExecution(controllerType);
            final Object controllerInstance = controllerType.getDeclaredConstructor().newInstance();
            final var viewName = controllerExecution.invoke(controllerInstance, request, response);
            move((String) viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Class<?> findController(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapper : handlerMappers) {
            final Class<?> handler = handlerMapper.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 handler입니다.");
    }

    private Method findControllerExecution(final Class<?> controllerType) {
        return Arrays.stream(controllerType.getDeclaredMethods())
                     .filter(DispatcherServlet::isExecuteMethod)
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("컨트롤러 실행 메서드가 존재하지 않습니다."));
    }

    private static boolean isExecuteMethod(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 2 && parameterTypes[0].equals(HttpServletRequest.class) && parameterTypes[1].equals(HttpServletResponse.class);
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
