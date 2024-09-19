package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Stream;

public class HandlerExecution {

    private final Method method;
    private final Object controllerInstance;

    public HandlerExecution(Method method, Class<?> controllerClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.method = method;
        this.controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        List<Object> preparedArgs = List.of(request, response);
        List<? extends Class<?>> requiredParameterClasses = Stream.of(method.getParameters())
                .map(Parameter::getType)
                .toList();

        List<Object> passingArgs = requiredParameterClasses.stream()
                .map(param -> preparedArgs.stream()
                        .filter(arg -> param.isAssignableFrom(arg.getClass()))
                        .findAny()
                        .orElse(null))
                .toList();

        return (ModelAndView) method.invoke(controllerInstance, passingArgs.toArray());
    }
}
