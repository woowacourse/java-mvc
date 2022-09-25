package nextstep.mvc.support;

import nextstep.mvc.exception.HandlerAdapterNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}
