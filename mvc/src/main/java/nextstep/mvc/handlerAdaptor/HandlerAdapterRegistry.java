package nextstep.mvc.handlerAdaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    private HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public static HandlerAdapterRegistry from() {
        return new HandlerAdapterRegistry(new ArrayList<>());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst();
    }
}
