package nextstep.mvc.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdaptor(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow();
    }
}
