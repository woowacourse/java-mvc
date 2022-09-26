package nextstep.mvc.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
    }

    public HandlerAdapterRegistry(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(AdapterNotFoundException::new);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
