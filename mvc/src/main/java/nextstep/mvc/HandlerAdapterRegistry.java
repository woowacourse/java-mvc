package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {
    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object controller) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(controller))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
