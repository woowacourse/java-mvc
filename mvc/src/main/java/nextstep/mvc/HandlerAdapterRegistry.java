package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public static HandlerAdapterRegistry empty() {
        return new HandlerAdapterRegistry(new ArrayList<>());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(object))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("요청을 처리할 수 있는 adapter가 없습니다."));
    }
}
