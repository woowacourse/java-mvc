package com.interface21;

import com.interface21.context.stereotype.HandlerManagement;
import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

public class HandlerManagementScanner {

    private HandlerManagementScanner() {}

    public static Set<Class<?>> scanHandlerHelper() {
        Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath());
        return reflections.getTypesAnnotatedWith(HandlerManagement.class, true);
    }

    public static <T> List<T> scanSubTypeOf(Class<?> clazz, Class<T> type) {
        Reflections reflections = new Reflections(clazz.getPackageName());
        return reflections.getSubTypesOf(type).stream()
                .map(HandlerManagementScanner::createObject)
                .map(type::cast)
                .toList();
    }

    private static Object createObject(Class<?> clazz) {
        try {
            return ReflectionUtils.accessibleConstructor(clazz).newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("기본 생성자가 존재하지 않습니다");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
