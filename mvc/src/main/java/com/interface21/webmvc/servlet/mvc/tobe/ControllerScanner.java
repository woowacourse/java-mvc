package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner implements HandlerMapping{

    private final Reflections reflections;

    public ControllerScanner(final Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }

    public Map<Class<?>, Object> getHandler()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // @Controller 붙은 클래스 찾기(클래스를 찾는 거라서 classes라고 명명했습니다)
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        Map<Class<?>, Object> controllers = new HashMap<>();

        // 찾은 클래스를 돌면서 인스턴스 생성하기
        for (Class<?> clazz : classes) {
            Object object = clazz.getDeclaredConstructor().newInstance();
            controllers.put(clazz, object);
        }

        return controllers;
    }

    @Override
    public void initialize() {

    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return null;
    }
}
