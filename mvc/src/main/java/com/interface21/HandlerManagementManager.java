package com.interface21;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerManagementManager {

    private static final Logger log = LoggerFactory.getLogger(HandlerManagementManager.class);
    private static final Map<String, Object> MANAGERS = new ConcurrentHashMap<>();
    private static final Set<Class<?>> HANDLER_CLASSES = HandlerManagementScanner.scanHandlerHelper();

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

    public <T> List<T> getHandler(Class<T> clazz) {
        return MANAGERS.values().stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }
}
