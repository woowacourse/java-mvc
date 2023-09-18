package nextstep.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new ArrayList<>(handlerAdapters);
    }

    public HandlerAdapters() {
        this(Collections.emptyList());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public Optional<HandlerAdapter> findHandlerAdapter(final Object handler) {
        return this.handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findAny();
    }
}
