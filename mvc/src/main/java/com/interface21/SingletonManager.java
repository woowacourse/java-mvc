package com.interface21;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.Reflections;

public class SingletonManager {

    private static final Map<String, Object> singletons = new ConcurrentHashMap<>();

    private SingletonManager() {}

    private static class Singleton {
        private static final SingletonManager INSTANCE = new SingletonManager();
    }

    public static SingletonManager getInstance() {
        return Singleton.INSTANCE;
    }

    private void registerSingleton(Object object) {
        String clazzName = object.getClass().getName();
        if (singletons.containsKey(clazzName)) {
            return;
        }
        singletons.put(clazzName, object);
    }

    public <T> List<T> getSubTypesOf(Class<T> clazz) {
        return singletons.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    public void registerHandler(Class<?> clazz) {
        Reflections reflections = new Reflections(clazz.getPackageName());
        reflections.getSubTypesOf(HandlerMapping.class).stream()
                .map(this::createObject)
                .forEach(this::registerSingleton);
        reflections.getSubTypesOf(HandlerAdapter.class).stream()
                .map(this::createObject)
                .forEach(this::registerSingleton);
    }

    private Object createObject(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new NotFoundException("기본 생성자가 존재하지 않습니다");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
