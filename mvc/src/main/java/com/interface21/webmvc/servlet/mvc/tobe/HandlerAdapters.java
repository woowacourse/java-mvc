package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.UncheckedReflectiveOperationException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerAdapters {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapters.class);

    private final Object[] basePackage;
    private final Set<HandlerAdapter> adapters;

    public HandlerAdapters(final Object... basePackage) {
        this.basePackage = getBasePackage(basePackage);
        this.adapters = new HashSet<>();
    }

    private Object[] getBasePackage(final Object... basePackage) {
        if (ArrayUtils.isEmpty(basePackage)) {
            return new Object[]{getClass().getPackageName()};
        }
        Object[] basePackages = Arrays.copyOf(basePackage, basePackage.length + 1);
        basePackages[basePackages.length - 1] = getClass().getPackageName();
        return basePackages;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<HandlerAdapter> handlerAdapters = reflections.getSubTypesOf(HandlerAdapter.class)
                .stream()
                .map(this::getInstance)
                .collect(Collectors.toUnmodifiableSet());
        adapters.addAll(handlerAdapters);
        log.info("Initialize HandlerAdapters - " + "Adapters Size : " + adapters.size());
        adapters.forEach(adapter -> log.info("Adapter : " + adapter.getClass().getName()));
    }

    private HandlerAdapter getInstance(final Class<?> clazz) {
        try {
            return (HandlerAdapter) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 InvocationTargetException |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw new UncheckedReflectiveOperationException(e);
        }
    }

    public ModelAndView handle(final Object handler,
                               final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        for (HandlerAdapter adapter : adapters) {
            if (adapter.canHandle(handler)) {
                return adapter.handle(handler, request, response);
            }
        }
        throw new NoSuchElementException("지원 가능한 핸들러가 없습니다.");
    }
}
