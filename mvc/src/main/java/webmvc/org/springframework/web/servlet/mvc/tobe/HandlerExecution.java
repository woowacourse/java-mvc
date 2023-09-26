package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MappingJackson2HttpMessageConverter;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var parameterTypes = method.getParameterTypes();
        if (parameterTypes.length > 2 && MediaType.isJson(request.getContentType())) {
            /// TODO: 2023/09/23 하나의 body가 아니라 다른 방식으로도 받을 수 있게 하기
            final Class<?> parameterType = parameterTypes[2];
            final Object requestBody = MappingJackson2HttpMessageConverter.readRequestBody(request, parameterType);
            return (ModelAndView) method.invoke(controller, request, response, requestBody);
        }

        return (ModelAndView) method.invoke(controller, request, response);
    }

    public String getMethodName() {
        return method.getName();
    }
}
