package nextstep.mvc.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter adapter) {
        this.handlerAdapters.add(adapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return this.handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No matched handlerAdapter for handler : " + handler));
    }
}
