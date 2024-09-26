package com.interface21;

import com.interface21.context.stereotype.Controller;
import com.interface21.context.stereotype.HandlerManagement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerManagementManager {

    private static final Logger log = LoggerFactory.getLogger(HandlerManagementManager.class);
    private static final Map<String, Object> MANAGERS = new ConcurrentHashMap<>();
    private static final Set<Class<?>> HANDLER_CLASSES = HandlerManagementScanner.scanHandlerHelper(HandlerManagementManager.class, HandlerManagement.class);

    private HandlerManagementManager() {}

    private static class Singleton {

        private static final HandlerManagementManager INSTANCE = new HandlerManagementManager();
    }
    public static HandlerManagementManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void initialize(Class<?> clazz) {
        registerHandlerManagement(clazz);
        registerHandlerManagement(this.getClass());
        registerAnnotation(clazz);
    }

    private void registerAnnotation(Class<?> clazz) {
        HandlerManagementScanner.scanHandlerHelper1(clazz, Controller.class)
                .forEach(this::registerHandlerManagement);
    }

    public void registerHandlerManagement(Class<?> clazz) {
        for (Class<?> handlerClass : HANDLER_CLASSES) {
            HandlerManagementScanner.scanSubTypeOf(clazz, handlerClass)
                    .forEach(this::registerHandlerManagement);
        }
    }

    private void registerHandlerManagement(Object object) {
        String clazzName = object.getClass().getName();
        if (MANAGERS.containsKey(clazzName)) {
            log.info("이미 등록되어 있는 클래스입니다");
            return;
        }
        MANAGERS.put(clazzName, object);
    }

    public List<Object> getAnnotationHandler(Class<? extends Annotation> controllerClass) {
        return MANAGERS.values().stream()
                .filter(clazz -> clazz.getClass().isAnnotationPresent(controllerClass))
                .toList();
    }

    public <T> List<T> getHandler(Class<T> clazz) {
        return MANAGERS.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }
}
