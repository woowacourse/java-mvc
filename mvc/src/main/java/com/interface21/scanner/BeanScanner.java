package com.interface21.scanner;

import com.interface21.container.BeanCreationException;
import com.interface21.context.stereotype.Component;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.reflections.Reflections;

public class BeanScanner {

    private BeanScanner() {
    }

    public static List<Object> componentScan(String packageName) {
        Reflections reflections = new Reflections(packageName);

        return reflections.getTypesAnnotatedWith(Component.class, true).stream()
                .map(BeanScanner::createBean)
                .toList();
    }

    public static List<Object> subTypeScan(Class<?> parentsClass, String packageName) {
        Reflections reflections = new Reflections(packageName);

        return reflections.getSubTypesOf(parentsClass).stream()
                .map(BeanScanner::createBean)
                .toList();
    }

    private static Object createBean(Class<?> clazz) {
        try {
            return ReflectionUtils.accessibleConstructor(clazz).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new BeanCreationException("빈을 생성할 수 없습니다.", e);
        }
    }
}
