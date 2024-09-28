package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.support.HandlerMethodArgumentResolver;
import com.interface21.web.support.HttpServletRequestResolver;
import com.interface21.web.support.HttpServletResponseResolver;
import com.interface21.web.support.NotFoundMethodArgumentResolver;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HandlerExecution {

    private final Class<?> controllerClass;
    private final Method method;
    private final List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();


    public HandlerExecution(Class<?> controllerClass, Method method) {
        this.controllerClass = controllerClass;
        this.method = method;

        resolvers.add(new HttpServletRequestResolver());
        resolvers.add(new HttpServletResponseResolver());
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object controller = controllerClass.getConstructor().newInstance();
        Object[] parameters = initParameters(request, response);

        return (ModelAndView) method.invoke(controller, parameters);
    }

    private Object[] initParameters(final HttpServletRequest request, final HttpServletResponse response) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            HandlerMethodArgumentResolver resolver = findSupportResolver(parameterType);
            parameters[i] = resolver.resolveArgument(request, response, parameterType);
        }
        return parameters;
    }

    private HandlerMethodArgumentResolver findSupportResolver(Class<?> parameterType) {
        return resolvers.stream()
                .filter(resolver -> resolver.supportsParameter(parameterType))
                .findFirst()
                .orElseThrow(() -> new NotFoundMethodArgumentResolver(
                        "지원하지 않는 파라미터 타입(%s)입니다.".formatted(parameterType.getName())
                ));
    }
}
