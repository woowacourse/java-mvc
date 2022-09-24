package nextstep.mvc.controller.registry;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
            .filter(it -> it.supports(object))
            .findAny()
            .orElseThrow();
    }
}

