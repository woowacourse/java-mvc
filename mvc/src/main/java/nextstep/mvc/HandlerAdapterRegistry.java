package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
