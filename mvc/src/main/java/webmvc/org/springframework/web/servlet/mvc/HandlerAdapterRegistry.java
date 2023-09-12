package webmvc.org.springframework.web.servlet.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
        this(Collections.emptyList());
    }

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters.addAll(handlerAdapters);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("invalid handler!"));
    }
}
