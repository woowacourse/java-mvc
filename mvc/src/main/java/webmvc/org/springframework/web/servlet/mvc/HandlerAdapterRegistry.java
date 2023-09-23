package webmvc.org.springframework.web.servlet.mvc;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class HandlerAdapterRegistry {

    private final Set<HandlerAdapter> handlerAdapters = new LinkedHashSet<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(found -> found.supports(handler))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("cannot find supporting handler adapter"));
    }

}
