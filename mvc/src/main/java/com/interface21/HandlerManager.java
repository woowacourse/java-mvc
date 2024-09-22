package com.interface21;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerManager {

    private static final Logger log = LoggerFactory.getLogger(HandlerManager.class);
    private static final Map<String, Object> singletons = new ConcurrentHashMap<>();

    private final List<Class<?>> handlerClasses = List.of(HandlerMapping.class, HandlerAdapter.class);

    private HandlerManager() {

    }

    private static class Singleton {
        private static final HandlerManager INSTANCE = new HandlerManager();
    }

    public static HandlerManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void initialize(Class<?> clazz) {
        registerHandler(clazz);
        registerHandler(this.getClass());
    }

    public void registerHandler(Class<?> clazz) {
        for (Class<?> handlerClass : handlerClasses) {
            HandlerScanner.scanSubTypeOf(clazz, handlerClass)
                    .forEach(this::registerHandler);
        }
    }

    private void registerHandler(Object object) {
        String clazzName = object.getClass().getName();
        if (singletons.containsKey(clazzName)) {
            log.info("이미 등록되어 있는 클래스입니다");
            return;
        }
        singletons.put(clazzName, object);
    }

    public <T> List<T> getHandler(Class<T> clazz) {
        return singletons.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }
}
