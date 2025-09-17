package com.interface21.core.util;

import com.interface21.context.stereotype.Controller;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public static Map<Class<?>, Object> scanControllers(final Object... basePackage) {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (Class<?> controller : controllerTypes) {
            try {
                controllers.put(controller, controller.getDeclaredConstructor().newInstance());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(controller.getSimpleName() + "의 기본 생성자를 찾을 수 없습니다.");
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(controller.getSimpleName() + "의 기본 생성자를 실행하는 과정에서 예외가 발생했습니다.");
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(controller.getSimpleName() + "의 기본 생성자에 접근할 수 없습니다.");
            } catch (InstantiationException e) {
                throw new IllegalStateException(controller.getSimpleName() + "는 추상 클래스입니다.");
            }
        }

        return controllers;
    }
}
