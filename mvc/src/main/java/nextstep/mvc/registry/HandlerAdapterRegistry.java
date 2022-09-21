package nextstep.mvc.registry;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    private HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapterRegistry() {
        this(new ArrayList<>());
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter findAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Failed to find adapter : " + handler.getClass()));
    }
}
