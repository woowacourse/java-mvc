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
                .orElseThrow(() -> new RuntimeException("적절한 핸들러 어댑터가 없습니다"));
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
