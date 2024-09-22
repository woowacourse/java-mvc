package com.interface21;

import com.interface21.context.stereotype.Register;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

public class HandlerScanner {

    public static Set<Class<?>> scanHandlerHelper() {
        Reflections reflections = new Reflections(ClasspathHelper.forJavaClassPath());
        return reflections.getTypesAnnotatedWith(Register.class);
    }

    public static <T> List<T> scanSubTypeOf(Class<?> clazz, Class<T> type) {
        Reflections reflections = new Reflections(clazz.getPackageName());
        return reflections.getSubTypesOf(type).stream()
                .map(HandlerScanner::createObject)
                .map(type::cast)
                .toList();
    }

    private static Object createObject(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new NotFoundException("기본 생성자가 존재하지 않습니다");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
