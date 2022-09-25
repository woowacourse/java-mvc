package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Couldn't find adaptor for handler"));
    }
}
