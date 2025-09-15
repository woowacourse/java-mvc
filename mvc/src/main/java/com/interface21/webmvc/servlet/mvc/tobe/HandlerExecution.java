package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class HandlerExecution {

    private final Object controller;

    public HandlerExecution(Object controller) {
        this.controller = controller;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Method method = findMethod(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Method findMethod(String uri, RequestMethod requestMethod) {
        List<Method> allMethods = List.of(controller.getClass().getMethods());
        for (Method method : allMethods) {
            validateRequestMappingAnnotation(method);
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            if (annotation.value().equals(uri) && annotation.method()[0] == requestMethod) {
                return method;
            }
        }
        throw new IllegalArgumentException("해당하는 메서드가 존재하지 않습니다.");
    }

    private void validateRequestMappingAnnotation(Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        if (annotation == null) {
            throw new IllegalArgumentException("RequestMapping 애노테이션이 적용되지 않은 메서드를 등록하려고 시도하고 있습니다.");
        }
        if (annotation.value() == null || annotation.value().isEmpty() || annotation.method().length == 0) {
            throw new IllegalArgumentException("RequestMapping 애노테이션의 값이 올바르지 않습니다.");
        }
    }
}
