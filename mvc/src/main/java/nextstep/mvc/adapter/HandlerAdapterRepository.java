package nextstep.mvc.adapter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class HandlerAdapterRepository {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRepository() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void init() {
        final Reflections adapterReflections = new Reflections("nextstep.mvc.adapter", Scanners.SubTypes);
        final Set<Class<? extends HandlerAdapter>> adapterTypes = adapterReflections.getSubTypesOf(
                HandlerAdapter.class);
        for (Class<? extends HandlerAdapter> adapterType : adapterTypes) {
            addAdapterInstances(adapterType);
        }
    }

    public HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void addAdapterInstances(final Class<? extends HandlerAdapter> adapterType) {
        try {
            final HandlerAdapter handlerAdapter = adapterType.getDeclaredConstructor().newInstance();
            handlerAdapters.add(handlerAdapter);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
