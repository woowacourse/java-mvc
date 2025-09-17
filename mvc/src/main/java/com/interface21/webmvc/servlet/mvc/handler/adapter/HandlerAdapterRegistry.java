package com.interface21.webmvc.servlet.mvc.handler.adapter;

import com.interface21.core.util.ReflectionUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final Object[] basePackages;


    public HandlerAdapterRegistry(final Object... basePackages) {
        this.basePackages = basePackages;
    }

    public void initialize() throws Exception {
        final Reflections reflections = new Reflections(basePackages);
        final Set<Class<? extends HandlerAdapter>> classes = reflections.getSubTypesOf(HandlerAdapter.class);
        for (Class<? extends HandlerAdapter> clazz : classes) {
            final HandlerAdapter handlerAdapter = createInstance(clazz);
            addHandlerAdapter(handlerAdapter);
        }
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }

        throw new IllegalArgumentException("Invalid type of Handler");
    }

    private static HandlerAdapter createInstance(final Class<? extends HandlerAdapter> clazz)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Constructor<? extends HandlerAdapter> constructor = ReflectionUtils.accessibleConstructor(clazz);

        return constructor.newInstance();
    }
}
