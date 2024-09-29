package com.interface21.context.container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

public class Container {

    private static class SingletonHolder {
        private static final Container INSTANCE = new Container();
    }

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private final Map<String, Object> container;

    private Container() {
        container = new ConcurrentHashMap<>();
    }

    public static Container getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void run(final Class<?> app) {
        Container container = getInstance();
        container.mvcScan();
        container.appScan(app);
    }

    private void mvcScan() {
        Reflections reflections = new Reflections("com.interface21");
        scan(reflections);
    }

    private void appScan(final Class<?> app) {
        String packageName = app.getPackageName();
        Reflections reflections = new Reflections(packageName);
        scan(reflections);
    }

    private void scan(final Reflections reflections) {
        registerAnnotatedInstancesOf(reflections, com.interface21.context.stereotype.Controller.class);
        registerInstancesOf(reflections, Controller.class);
        registerInstancesOf(reflections, HandlerMapping.class);
        registerInstancesOf(reflections, HandlerAdapter.class);
    }

    private void registerAnnotatedInstancesOf(
            final Reflections reflections,
            final Class<? extends Annotation> annotation
    ) {
        reflections.getTypesAnnotatedWith(annotation).forEach(this::register);
    }

    private <T> void registerInstancesOf(final Reflections reflections, final Class<T> type) {
        reflections.getSubTypesOf(type).forEach(this::register);
    }

    private <T> void register(final Class<T> type) {
        String name = type.getSimpleName();
        Object instance;
        try {
            Constructor<T> constructor = ReflectionUtils.accessibleConstructor(type);
            instance = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Instance registration failed on class: %s".formatted(name), e);
        }
        container.put(name, instance);
        log.info("{} instance registered", name);
    }

    public <T> List<T> getInstancesOf(final Class<T> type) {
        return container.values()
                .stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    public List<Object> getInstancesAnnotatedOf(final Class<? extends Annotation> annotation) {
        return container.values()
                .stream()
                .filter(instance -> instance.getClass().isAnnotationPresent(annotation))
                .toList();
    }
}
