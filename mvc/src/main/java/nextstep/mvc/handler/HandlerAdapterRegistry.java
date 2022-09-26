package nextstep.mvc.handler;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.exception.NotFoundHandlerAdapterException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
            .filter(it -> it.supports(handler))
            .findFirst()
            .orElseThrow(NotFoundHandlerAdapterException::new);
    }
}
