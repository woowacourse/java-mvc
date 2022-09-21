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

    HandlerAdapter getHandlerAdaptor(final Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdaptor -> handlerAdaptor.supports(handler))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Couldn't find adaptor for handler"));
    }
}
