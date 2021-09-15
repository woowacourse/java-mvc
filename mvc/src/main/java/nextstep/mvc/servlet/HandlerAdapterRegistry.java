package nextstep.mvc.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HandlerAdapterRegistry {
    private static final Logger log = LoggerFactory.getLogger(HandlerAdapterRegistry.class);

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElse(null);
        log.info("handlerAdapter: {}", handlerAdapter.getClass().getName());
        return handlerAdapter;
    }
}
