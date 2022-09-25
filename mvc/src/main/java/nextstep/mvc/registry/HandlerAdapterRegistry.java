package nextstep.mvc.registry;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.exception.NotFoundHandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter){
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(h -> h.supports(handler))
                .findFirst()
                .orElseThrow(NotFoundHandlerAdapter::new);
    }

}
