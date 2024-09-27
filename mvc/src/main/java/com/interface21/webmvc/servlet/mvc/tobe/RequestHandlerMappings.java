package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.UncheckedReflectiveOperationException;
import org.reflections.Reflections;

public class RequestHandlerMappings {

    private final Object[] basePackage;
    private final Set<RequestHandlerMapping> mappings;

    public RequestHandlerMappings(final Object... basePackage) {
        this.basePackage = getBasePackage(basePackage);
        this.mappings = new LinkedHashSet<>();
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
        Set<RequestHandlerMapping> requestHandlerMappings = reflections.getSubTypesOf(RequestHandlerMapping.class)
                .stream()
                .map(this::getInstance)
                .collect(Collectors.toUnmodifiableSet());
        mappings.addAll(requestHandlerMappings);
        mappings.forEach(RequestHandlerMapping::initialize);
    }

    private RequestHandlerMapping getInstance(final Class<?> clazz) {
        try {
            return (RequestHandlerMapping) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 InvocationTargetException |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw new UncheckedReflectiveOperationException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return mappings.stream()
                .map(requestHandlerMapping -> requestHandlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new ForwardController("/404.jsp"));
    }
}
