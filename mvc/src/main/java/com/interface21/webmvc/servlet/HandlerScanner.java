package com.interface21.webmvc.servlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.reflections.Reflections;

public class HandlerScanner {

    private final Reflections reflections;
    private final List<String> controllerBasePackages;

    public HandlerScanner(final List<String> handlerBasePackages, final List<String> controllerBasePackages) {
        final var handlerPackages = List.copyOf(Objects.requireNonNull(handlerBasePackages, "핸들러 검색 패키지는 null일 수 없습니다."));
        this.controllerBasePackages = List.copyOf(Objects.requireNonNull(controllerBasePackages, "컨트롤러 검색 패키지는 null일 수 없습니다."));
        this.reflections = new Reflections(handlerPackages.toArray());
    }

    public List<HandlerMapping> getHandlerMappings() {
        final var handlerMappings = getHandlers(HandlerMapping.class, controllerBasePackages.toArray());
        handlerMappings.forEach(HandlerMapping::initialize);
        handlerMappings.sort(Comparator.comparingInt(HandlerMapping::getOrder));
        return List.copyOf(handlerMappings);
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return List.copyOf(getHandlers(HandlerAdapter.class));
    }

    private <T> List<T> getHandlers(final Class<T> handlerType, final Object... constructorArgs) {
        final List<T> handlers = new ArrayList<>();
        reflections.getSubTypesOf(handlerType)
                .stream()
                .filter(handlerClass -> !handlerClass.isInterface())
                .filter(handlerClass -> !Modifier.isAbstract(handlerClass.getModifiers()))
                .sorted(Comparator.comparing(Class::getName))
                .forEach(handlerClass -> handlers.add(createHandler(handlerClass, constructorArgs)));
        return handlers;
    }

    private <T> T createHandler(final Class<? extends T> handlerClass, final Object[] constructorArgs) {
        try {
            final var constructor = getConstructor(handlerClass);
            if (constructor.getParameterCount() == 0) {
                return constructor.newInstance();
            }
            return constructor.newInstance((Object) constructorArgs);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(handlerClass.getName() + " 생성에 실패했습니다.", e);
        }
    }

    private <T> Constructor<? extends T> getConstructor(final Class<? extends T> handlerClass)
            throws NoSuchMethodException {
        try {
            return handlerClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            return handlerClass.getDeclaredConstructor(Object[].class);
        }
    }
}
