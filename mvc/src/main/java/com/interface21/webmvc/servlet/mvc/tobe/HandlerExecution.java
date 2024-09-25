package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

public class HandlerExecution {
    private static final Map<Class<?>, Object> controllerInstances = new ConcurrentHashMap<>();

    private final Object runnerInstance;
    private final Method method;

    public HandlerExecution(Class<?> runnerInstanceClazz, Method method) {
        this.runnerInstance = controllerInstances.computeIfAbsent(runnerInstanceClazz, this::createNewInstance);
        this.method = method;
    }

    private Object createNewInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(String.format(
                    "컨트롤러 %s 를 생성할 수 있는 기본 생성자가 존재하지 않습니다.", clazz.getName())
            );
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(runnerInstance, request, response);
    }
}
