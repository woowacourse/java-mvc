package webmvc.org.springframework.web.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerAdapterFactory {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionAdapter.class);

    private final Map<Class<?>, Class<? extends HandlerAdapter>> supportedHandlerAdapters;
    private final Map<Object, HandlerAdapter> adapterCache;

    public HandlerAdapterFactory() {
        this.supportedHandlerAdapters = new HashMap<>();
        this.adapterCache = new ConcurrentHashMap<>();
    }

    public void addAdapterType(final Class<?> handlerType, final Class<? extends HandlerAdapter> adapterType) {
        log.info("Add handler adapter - {}", adapterType.getSimpleName());
        supportedHandlerAdapters.put(handlerType, adapterType);
    }

    public HandlerAdapter getAdapter(final Object handler) {
        if (!adapterCache.containsKey(handler)) {
            adapterCache.put(handler, makeHandlerAdapter(handler));
        }
        return adapterCache.get(handler);
    }

    private HandlerAdapter makeHandlerAdapter(final Object handler) {
        final Class<? extends HandlerAdapter> adapterClass = supportedHandlerAdapters.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isInstance(handler))
                .map(Map.Entry::getValue)
                .findAny()
                .orElseThrow(() -> new RuntimeException("Could not find supported adapter for " + handler.getClass().getSimpleName()));

        try {
            return adapterClass.getDeclaredConstructor(Object.class).newInstance(handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
